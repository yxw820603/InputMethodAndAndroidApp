#include <iostream>
#include "status_manager.h"

int main() {
    StatusManager statusManager;
    statusManager.ToggleStatus();
    if (statusManager.IsEnabled()) {
        std::cout << "Input method enabled" << std::endl;
    } else {
        std::cout << "Input method disabled" << std::endl;
    }
    return 0;
}
