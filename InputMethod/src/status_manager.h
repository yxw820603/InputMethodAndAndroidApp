#ifndef STATUS_MANAGER_H
#define STATUS_MANAGER_H

class StatusManager {
public:
    StatusManager();
    void ToggleStatus();
    bool IsEnabled() const;

private:
    bool enabled_;
};

#endif // STATUS_MANAGER_H
