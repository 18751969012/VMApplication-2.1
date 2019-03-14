//串口封装函数
#include <sys/ioctl.h>
#include <sys/types.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <termios.h>
#include <fcntl.h>
#include <pthread.h>
#include <syslog.h>
#include <signal.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <sched.h>
#include <signal.h>
#include <errno.h>
#include <unistd.h>
#include <ctype.h>
#include <string.h>
#include <sys/time.h>
#include "Log.h"
#include <sys/stat.h>

#define   STX      0x02
#define   ETX      0x03
#define    INVALID_HANDLE_VALUE    -1
#define    PORTNUM        10
#define   SERIAL_READ_CONTINUE_BYTE

static int CommPort[PORTNUM] = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
struct termios oldtio[PORTNUM], options[PORTNUM];
//static fd_set Fds;

static int com_baud[PORTNUM] = {0,};
const char *devname[] = {
        "/dev/ttyS0",
        "/dev/ttyS1",
        "/dev/ttyS2",
        "/dev/ttyS3",
        "/dev/ttyS4",
        "/dev/ttyO2",
        "/dev/ttyO3",
        "/dev/ttyO4",
        "/dev/ttyAMA2",
        "/dev/ttyAMA0"
};

#define  REN_DEV_PATH                    "/dev/iarkpad"
#define     _UART_USED_485_RECEIVED        100
#define     _UART_USED_485_SEND            101
static int ren_fd;

int
SetArrt(int Comm, int baud, unsigned char databits, unsigned char stopbits, unsigned char parity) {
    int ret, flags;
    int baudrate;
    com_baud[Comm] = baud;
    LOGV("\nComm:%d baud:%d bits:%d stopbits:%d parity:%c\n", Comm, baud, databits, stopbits,
         parity);

    if (Comm < 0 || Comm > 10)
        return -1;

    if (CommPort[Comm] == INVALID_HANDLE_VALUE)
        return -1;

    tcgetattr(CommPort[Comm], &oldtio[Comm]);
    tcgetattr(CommPort[Comm], &options[Comm]);

    //bzero(&options[Comm],sizeof(struct termios));

    options[Comm].c_lflag = 0;

#if 1
    options[Comm].c_iflag &= ~(INLCR | ICRNL | IGNCR);
    options[Comm].c_iflag &= ~(IXON | IXOFF | IXANY);

    options[Comm].c_oflag &= ~(ONLCR | OCRNL);
#endif

    options[Comm].c_cc[VTIME] = 0;
    options[Comm].c_cc[VMIN] = 0;

    options[Comm].c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG);//rockchip add

    //options[Comm].c_cflag &= ~CNEW_RTSCTS;
    options[Comm].c_cflag &= ~CSIZE;
    switch (databits) /*设置数据位数*/
    {
        case 5:
            options[Comm].c_cflag |= CS5;
            break;
        case 6:
            options[Comm].c_cflag |= CS6;
            break;
        case 7:
            options[Comm].c_cflag |= CS7; //7位数据位
            break;
        case 8:
            options[Comm].c_cflag |= CS8; //8位数据位
            break;
        default:
            options[Comm].c_cflag |= CS8;
            break;
    }
    options[Comm].c_cflag |= CLOCAL | CREAD;//rockchip modify

    switch (parity) //设置校验
    {
        case 'n':
        case 'N':
            options[Comm].c_cflag &= ~PARENB;   /* 输出不进行奇偶校验 */
            options[Comm].c_iflag &= ~INPCK;     /* 输入不进行奇偶校验 */
            break;
        case 'o':
        case 'O':
            options[Comm].c_cflag |= (PARODD | PARENB); /* 设置为奇效验*/
            options[Comm].c_iflag |= INPCK;             /* Disnable parity checking */
            break;
        case 'e':
        case 'E':
            options[Comm].c_cflag |= PARENB;     /* Enable parity */
            options[Comm].c_cflag &= ~PARODD;   /* 转换为偶效验*/
            options[Comm].c_iflag |= INPCK;       /* Disnable parity checking */
            break;
        default:
            options[Comm].c_cflag &= ~PARENB;   /* Clear parity enable */
            options[Comm].c_iflag &= ~INPCK;     /* Enable parity checking */
            break;
    }

    switch (stopbits)//设置停止位
    {
        case 1:
            options[Comm].c_cflag &= ~CSTOPB;  //1 stop bit
            break;
        case 2:
            options[Comm].c_cflag |= CSTOPB;  //2 stop bit
            break;
        default:
            options[Comm].c_cflag &= ~CSTOPB;
            break;
    }

    switch (baud) {
        case 2400:
            baudrate = B2400;
            break;
        case 4800:
            baudrate = B4800;
            break;
        case 9600:
            baudrate = B9600;
            break;
        case 19200:
            baudrate = B19200;
            break;
        case 57600:
            baudrate = B57600;
            break;
        case 115200:
            baudrate = B115200;
            break;
        case 38400:
            baudrate = B38400;
            break;
        default :
            baudrate = B9600;
            break;
    }

    cfsetispeed(&options[Comm], baudrate);
    cfsetospeed(&options[Comm], baudrate);

    ret = tcsetattr(CommPort[Comm], TCSAFLUSH, &options[Comm]);
    if (ret) {
        LOGE("\nSetArrt TCSAFLUSH err\n");
    }
#if 0
    else
    {
    usleep(200);
        ret = tcsetattr(CommPort[Comm],TCSANOW,&options[Comm]);
        if (ret != 0)
        {
            LOGE("\nSetArrt TCSANOW err\n");
        }
  }
#endif
    if (Comm == 1)        //1---used 485
    {
        //ren_fd=open(REN_DEV_PATH,O_RDONLY);
        ren_fd = open(REN_DEV_PATH, O_RDWR | O_NDELAY | O_NOCTTY);
        if (ren_fd < 0) {
            LOGE("\nSet 485 Ren IO err\n");
        }
        ioctl(ren_fd, _UART_USED_485_RECEIVED, 0);
    }

    return ret;
}

int
OpenCom(int Comm, int baud, unsigned char databits, unsigned char stopbits, unsigned char parity) {
    int ret = 0;

    if (Comm < 0 || Comm > 10)
        return -1;

    //LOGV("OpenCom:%s\n",devname[Comm]);

    if (CommPort[Comm] == INVALID_HANDLE_VALUE) {
        CommPort[Comm] = open(devname[Comm], O_RDWR | O_NOCTTY |
                                             O_NDELAY);//O_NONBLOCK|O_RDWR);//O_RDWR | O_NOCTTY | O_NDELAY
        if (CommPort[Comm] == INVALID_HANDLE_VALUE) {
            LOGE("Open file err:%s,Comm=%d", devname[Comm], Comm);
            return -1;
        }

        ret = SetArrt(Comm, baud, databits, stopbits, parity);
        if (ret) {
            LOGE("\nOpenCom err!\n");
            return ret;
        }

        //usleep(200);
        // 清除监测集合
        //FD_ZERO(&Fds);
        // 将串口句柄加入到监测集合中
        //FD_SET(CommPort[Comm], &Fds);
        return ret;
    }

    //LOGV("already open com:%d",Comm);
    return ret;
}

int WritePort(int Comm, unsigned char *buffer, int dwBytesToWrite) {
    int res;
    int i = 0;

    if (Comm < 0 || Comm > 10)
        return -1;

    if (CommPort[Comm] == INVALID_HANDLE_VALUE) {
        LOGE("\nNot open port\n");
        return -1;
    }

    if (Comm == 1)        //1---used 485
        ioctl(ren_fd, _UART_USED_485_SEND, 0);
    //usleep(100);
    while (1) {
        res = write(CommPort[Comm], buffer + i, 1);
        if (res > 0) {
            i++;
            if (i >= dwBytesToWrite)
                break;
        } else {
            LOGE("\nWritePort err\n");
            break;
        }
    }
    usleep(4000);
//write(CommPort[Comm],buffer,dwBytesToWrite);
    if (Comm == 1)        //1---used 485
        ioctl(ren_fd, _UART_USED_485_RECEIVED, 0);
    //usleep(100);

    return i;
}

int
ReadPort(int Comm, unsigned char *buffer, int nMaxLength, int TimeOutMs, unsigned char Ctlbyte) {

    int ret, i;
    int nread = -1;
    int count = 0;
    //unsigned long timeval,timeout;
    struct timeval CurTime;
    //struct timezone TimeZone;
    fd_set Fds;

    if (Comm < 0 || Comm > 10)
        return -1;

    if (CommPort[Comm] == INVALID_HANDLE_VALUE)
        return -1;

    FD_ZERO(&Fds);
    FD_SET(CommPort[Comm], &Fds);

    //LOGV("TimeOutMs:%d\n",TimeOutMs);

//#if 1
//
//	CurTime.tv_sec = TimeOutMs/1000;
//	CurTime.tv_usec = TimeOutMs%1000*1000;

//	if(Comm==1)		//1---used 485
//		ioctl(ren_fd,_UART_USED_485_RECEIVED,0);

//	//LOGV("select begin\n");
//
//  ret = select(CommPort[Comm]+1, &Fds, NULL, NULL, &CurTime);
//	if (ret == 0)
//	{
//		LOGE("\nReadPort timeout\n");
//		return -1;
//	}
//  else if (ret < 0)
//  {
//    LOGE("\nReadPort select error\n");
//    return -1;
//  }
//
//	//LOGV("select over\n");
//#else
//	ret=gettimeofday(&CurTime,&TimeZone);
//	if(ret<0){
//		LOGV("\ngettimeofday error\n");
//		return -1;
//	}
//	timeval=CurTime.tv_sec*1000000+CurTime.tv_usec;
//	timeout=timeval+TimeOutMs*1000;
//	while(timeout>=timeval){
//		ret = read(CommPort[Comm-1],buffer,nMaxLength);
//		if(ret>0)return ret;
//		else{
//			ret=gettimeofday(&CurTime,&TimeZone);
//			if(ret<0){
//				LOGV("\ngettimeofday error\n");
//				return -1;
//			}
//			timeval=CurTime.tv_sec*1000000+CurTime.tv_usec;
//		}
//	}
//#endif
    //memset(buffer, 0, sizeof(buffer));
    //usleep(((((com_baud[Comm]/1000)/8)) * nMaxLength)*1000);
    usleep(200000);
    while ((nread = read(CommPort[Comm], buffer + count, nMaxLength)) > 0) {
        // if(buffer[0] != Ctlbyte) continue;
        count += nread;
        // LOGV("nread = %d count = %d nMax = %d\n", nread, count,nMaxLength);
        if (count >= nMaxLength) {
#if 0
            for( i= 0;i < nMaxLength;i++) {
                    //LOGV("buff[%d] = %c\n",i,buffer[i]);
            }
#endif
            count = 0;
            return nMaxLength;
        }

    }
    //LOGV("read over\n");
    return count;
//    int len=0;
//    len=read(CommPort[Comm], buffer, nMaxLength);
//
//	return len;
}

void CloseComm(int Comm) {
    //LOGV("\nCloseComm");

    if (Comm < 0 || Comm > 10)
        return;

    if (CommPort[Comm] == INVALID_HANDLE_VALUE)
        return;

    //tcsetattr(CommPort[Comm],TCSAFLUSH,&oldtio[Comm]);
    close(CommPort[Comm]);
    CommPort[Comm] = INVALID_HANDLE_VALUE;
}

int SerOpen(int PortNo, int baud, unsigned char databits, unsigned char stopbits,
            unsigned char parity) {
    return OpenCom(PortNo, baud, databits, stopbits, parity);
}

void SerClose(int PortNo) {
    CloseComm(PortNo);
}

void SerClear(int PortNo)      //如果出现数据与规约不符合，可以调用这个函数来刷新串口读写数据
{
    tcflush(CommPort[PortNo], TCIOFLUSH);
    //usleep(200);
    // 清除监测集合
    //FD_ZERO(&Fds);
    // 将串口句柄加入到监测集合中
    //FD_SET(CommPort[PortNo], &Fds);
}

int SerWrite(int PortNo, unsigned char *pszBuf, unsigned int SendCnt, unsigned int TimeOut) {
    int fRet;
    //long i=0;
    //unsigned char LRC,TmpBuf[1024];

    //LOGV("\nSerWrite: PortNo=%d SendCnt=%d TimeOut=%d\n",PortNo,SendCnt,TimeOut);

    //tcsetattr(CommPort[PortNo],TCSAFLUSH,&options[PortNo]);
    //	TCIFLUSH刷清输入队列。
    //	TCOFLUSH刷清输出队列。
    //	TCIOFLUSH刷清输入、输出队列。
    tcflush(CommPort[PortNo], TCIOFLUSH);

    fRet = WritePort(PortNo, pszBuf, SendCnt);
    if (fRet == SendCnt)
        return 0;
    else
        return -1;
}

int SerRead(int PortNo, unsigned char *pszBuf, int ReadCnt, unsigned long TimeOut,
            unsigned char Ctlbyte) {
    int ret;
    int i = 0;

    //LOGV("\nSerRead:PortNo=%d TimeOut=%ld Ctlbyte=%x,Readcnt=%d\n",PortNo,TimeOut,Ctlbyte,ReadCnt);

#ifdef SERIAL_READ_CONTINUE_BYTE
    ret = ReadPort(PortNo, pszBuf, ReadCnt, TimeOut, Ctlbyte);
    if (ret < 0) {
        //LOGV("\nSerRead:get data timeout\n");
        return -1;
    }
#else
    for(i=0; i<Readcnt; i++){
        ret=ReadPort(PortNo,pszBuf+i, 1,(800>(TimeOut/ReadCnt))?800:TimeOut/ReadCnt);
        if(ret<0){
            //LOGV("\nSerRead:get data err1\n");
            SerClear(PortNo);
            return -1;
        }
    }
#endif

    return ret;
}

int InitSerPort(int PortNo) {
    int ret = 0;

    CommPort[PortNo] = INVALID_HANDLE_VALUE;

    //LOGV("\nInitDev over");
    return ret;
}

void CloseSerPort(int PortNo) {
    if (PortNo < 0 || PortNo > 10)
        return;

    CloseComm(PortNo);
    CommPort[PortNo] = INVALID_HANDLE_VALUE;
    //LOGV("\nCloseDev comport = %d",PortNo);
}

int GetSerState(int PortNo) {
    if (CommPort[PortNo] == INVALID_HANDLE_VALUE)
        return 0;
    else
        return 1;
}

