#include <fcitx/inputmethodengine.h>
#include <fcitx/addonfactory.h>
#include <fcitx/inputcontext.h>

#include <thread>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>

using namespace fcitx;

class UDPInput : public fcitx::InputMethodEngineV2
{
private:
    fcitx::InputContext *ic = nullptr;

    int udp = -1;
    std::thread th;
    bool isEnable = false;

    void loopCheck()
    {
        const int bufferSize = 1024;
        char buffer[bufferSize];
        struct sockaddr_in senderAddr;
        socklen_t senderAddrLen = sizeof(senderAddr);

        while (true)
        {
            memset(buffer, 0, bufferSize); // 清空缓冲区
            ssize_t bytesReceived = recvfrom(this->udp, buffer, bufferSize - 1, 0,
                                             (struct sockaddr *)&senderAddr, &senderAddrLen);
            if (bytesReceived < 0)
            {
                FCITX_ERROR() << strerror(errno);
            }
            else if (bytesReceived > 0 && this->isEnable)
            {
                //需要把buffer转换成std::string类型：
                std::string message(buffer, bytesReceived);
                // FCITX_WARN()<<"receive message: "<<message;
                this->ic->commitString(message);
            }

            // 暂停一段时间，避免 CPU 使用率过高
            std::this_thread::sleep_for(std::chrono::milliseconds(100));
        }
    }

public:
    void keyEvent(const fcitx::InputMethodEntry &entry, fcitx::KeyEvent &keyEvent) override
    {
        FCITX_UNUSED(entry);
        FCITX_INFO() << keyEvent.key() << " isRelease=" << keyEvent.isRelease();
    }

    void activate(const InputMethodEntry &entry, InputContextEvent &event) override
    {
        FCITX_UNUSED(entry);
        FCITX_UNUSED(event);
        FCITX_INFO() << "UDPInput Activate";

        if (ic == nullptr)
        {
            this->ic = event.inputContext();
        }

        if (udp == -1)
        {
            udp = socket(AF_INET, SOCK_DGRAM, 0);
            if (udp == -1)
            {
                FCITX_ERROR() << "socket error";
                return;
            }

            bool reuse = true;
            setsockopt(udp, SOL_SOCKET, SO_REUSEADDR, &reuse, sizeof(reuse));

            // 绑定端口
            sockaddr_in addr;
            addr.sin_family = AF_INET;
            addr.sin_port = htons(7300);

            // 绑定地址,使用组播地址
            addr.sin_addr.s_addr = inet_addr("224.0.0.1");
            if (bind(udp, (sockaddr *)&addr, sizeof(addr)) == -1)
            {
                FCITX_ERROR() << "bind error";
                return;
            }
        }
        if (!th.joinable())
        {
            th = std::thread(&UDPInput::loopCheck, this);
        }
        else
        {
            FCITX_ERROR() << "thread is running";
        }

        this->isEnable = true;
    }

    void deactivate(const InputMethodEntry &entry, InputContextEvent &event) override
    {
        reset(entry, event);
        this->isEnable = false;
    }
};

class UDPInputFactory : public fcitx::AddonFactory
{
    fcitx::AddonInstance *create(fcitx::AddonManager *manager) override
    {
        FCITX_UNUSED(manager);
        return new UDPInput();
    }
};

FCITX_ADDON_FACTORY(UDPInputFactory);