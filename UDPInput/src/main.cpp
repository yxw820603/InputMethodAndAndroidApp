#include <fcitx/inputmethodengine.h>
#include <fcitx/addonfactory.h>

class MyInputMethod : public fcitx::InputMethodEngineV2
{
public:
    void keyEvent(const fcitx::InputMethodEntry &entry, fcitx::KeyEvent &keyEvent) override{
         FCITX_UNUSED(entry);
        FCITX_INFO() << keyEvent.key() << " isRelease=" << keyEvent.isRelease();
    }
};

class MyInputMethodFactory : public fcitx::AddonFactory {
    fcitx::AddonInstance * create(fcitx::AddonManager * manager) override {
        FCITX_UNUSED(manager);
        return new MyInputMethod();
    }
};

FCITX_ADDON_FACTORY(MyInputMethodFactory);