#include "status_manager.h"

StatusManager::StatusManager() : enabled_(false) {}

void StatusManager::ToggleStatus() {
    enabled_ = !enabled_;
}

bool StatusManager::IsEnabled() const {
    return enabled_;
}
