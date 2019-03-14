#ifndef _SERCOMM_H
#define _SERCOMM_H

#define COM0       0
#define COM1       1
#define COM2       2
#define COM3       3
#define COM4       4

int SerOpen(int PortNo,int baud,unsigned char databits,unsigned char stopbits,unsigned char parity);
void SerClose(int PortNo);
void SerClear(int PortNo);
int  SerWrite(int PortNo, unsigned char *pszBuf , unsigned int SendCnt , unsigned int TimeOut);
int  SerRead(int PortNo, unsigned char *pszBuf, int ReadCnt,unsigned long TimeOut,unsigned char Ctlbyte);
int InitSerPort(int PortNo);
void CloseSerPort(int PortNo);
int GetSerState(int PortNo);

#endif
