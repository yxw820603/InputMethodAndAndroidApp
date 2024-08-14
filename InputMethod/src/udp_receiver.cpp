#include "udp_receiver.h"
#include <iostream>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <cstring>

void UdpReceiver::startReceiving(const char* multicastAddress) {
    int sockfd;
    struct sockaddr_in addr;
    struct ip_mreq mreq;
    char msgbuf[256];
    int addrlen, nbytes;

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd < 0) {
        perror("Socket creation failed");
        return;
    }

    memset(&addr, 0, sizeof(addr));
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = htonl(INADDR_ANY);
    addr.sin_port = htons(4446);

    if (bind(sockfd, (struct sockaddr *) &addr, sizeof(addr)) < 0) {
        perror("Bind failed");
        return;
    }

    mreq.imr_multiaddr.s_addr = inet_addr(multicastAddress);
    mreq.imr_interface.s_addr = htonl(INADDR_ANY);

    if (setsockopt(sockfd, IPPROTO_IP, IP_ADD_MEMBERSHIP, &mreq, sizeof(mreq)) < 0) {
        perror("setsockopt failed");
        return;
    }

    while (true) {
        addrlen = sizeof(addr);
        nbytes = recvfrom(sockfd, msgbuf, sizeof(msgbuf), 0, (struct sockaddr *) &addr, (socklen_t*)&addrlen);
        if (nbytes < 0) {
            perror("recvfrom failed");
            return;
        }
        msgbuf[nbytes] = '\0';
        std::cout << "Received: " << msgbuf << std::endl;
    }
}
