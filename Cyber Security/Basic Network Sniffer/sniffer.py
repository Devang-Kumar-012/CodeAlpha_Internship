from scapy.all import sniff, IP, TCP, UDP, ICMP

def process_packet(packet):
    if IP in packet:
        ip_layer = packet[IP]
        print(f"[+] Packet: {ip_layer.src} -> {ip_layer.dst} | Protocol: {ip_layer.proto}")
        if packet.haslayer(TCP):
            print(" - TCP Segment")
        elif packet.haslayer(UDP):
            print(" - UDP Segment")
        elif packet.haslayer(ICMP):
            print(" - ICMP Packet")
        print("-" * 50)

print("🌐 Starting Packet Sniffer... Press CTRL+C to stop.")
sniff(prn=process_packet, store=0)