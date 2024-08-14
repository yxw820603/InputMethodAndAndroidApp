#include <iostream>
#include "config.h"
#include "status_manager.h"
#include "udp_receiver.h"

int main() {
    StatusManager statusManager;
    UdpReceiver udpReceiver;
    statusManager.ToggleStatus();
    if (statusManager.IsEnabled()) {
        std::cout << "Input method enabled" << std::endl;
        udpReceiver.startReceiving(DEFAULT_MULTICAST_ADDRESS);
    } else {
        std::cout << "Input method disabled" << std::endl;
    }
    return 0;
}
