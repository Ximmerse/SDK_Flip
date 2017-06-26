// vs2015.cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"
#include "xdevice.h"
#include <windows.h>



int get_int() {

	int value;
	while (scanf("%d", &value) != 1) {
		while (getchar() != '\n');
		printf("The format is not correct. Please re-enter it.！\n");
	}
	while (getchar() != '\n');
	return value;
}

char get_first() {
	char value;
	scanf("%c", &value);
	while (value == '\n' || value == 32)
	{
		scanf("%c", &value);
		while (getchar() != '\n');
	}
	while (getchar() != '\n');
	return value;
}


int menu() {
	int result;
	printf("*************************************************************\n");
	printf(" Please select：\n");
	printf(" 1.Get controller status\n");
	printf(" 2.Get controller event\n");
	printf("*************************************************************\n");
	result = get_int();
	while (result > 2 || result < 1) {
		printf("Please input 1-2！\n");
		result = get_int();
	}
	return result;
}


void func001() {
	char ch;

	XDeviceInit();
	int handle=XDeviceGetInputDeviceHandle("XCobra-0");

	if (handle < 0) {
		printf("get Input handle %d error, i\n");
	}

	XDeviceUpdateInputState(handle);
	int status = XDeviceGetInt(handle, kField_ConnectionState, 0);
	switch (status) {
	case kConnectionState_Disconnected: printf("device is Disconnected\n"); break;
	case kConnectionState_Scanning:     printf("device is Scanning\n"); break;
	case kConnectionState_Connecting:   printf("device is Connecting\n"); break;
	case kConnectionState_Connected:    printf("device is Connected\n"); break;
	case kConnectionState_Error:        printf("device is Error:%d\n", XDeviceGetInt(handle, kField_ConnectionState, 0)); break;
	default:break;
	}

	XDeviceExit();
}

void func002() {
	char ch;
	ControllerState *state=(ControllerState*)malloc(sizeof(ControllerState));
	int preTimestamp=0;

	printf("*************************************************************\n");
	printf(" Press the key：\n");
	printf(" 'c': exit \n");
	printf(" 'r': yaw recenter \n");
	printf(" 'b': get battery level\n");
	printf("*************************************************************\n");


	XDeviceInit();
	int handle = XDeviceGetInputDeviceHandle("XCobra-0");
	if (handle < 0) {
		printf("get Input handle error\n");
	}

	do {
		int errorCode = XDeviceGetInputState(handle, state);
	
		if (errorCode >= 0 && state->timestamp!= preTimestamp) {
			printf("rotation       %.4f, %.4f, %.4f, %.4f\n", state->rotation[0], state->rotation[2], state->rotation[3], state->rotation[4]);
			printf("accelerometer  %.6f, %.6f, %.6f \n", state->accelerometer[0], state->accelerometer[1], state->accelerometer[2]);
			printf("gyroscope      %.6f, %.6f, %.6f \n", state->gyroscope[0], state->gyroscope[1], state->gyroscope[2]);
			printf("buttons: ");
			printf("%s", (state->buttons & CONTROLLER_BUTTON_START)==0?"":"START ");
			printf("%s", (state->buttons & CONTROLLER_BUTTON_BACK) == 0 ? "" : "BACK ");
			printf("%s", (state->buttons & CONTROLLER_BUTTON_LEFT_SHOULDER) == 0 ? "" : "LEFT_SHOULDER ");
			printf("%s", (state->buttons & CONTROLLER_BUTTON_RIGHT_SHOULDER) == 0 ? "" : "RIGHT_SHOULDER ");
			printf("%s", (state->buttons & CONTROLLER_BUTTON_LEFT_THUMB) == 0 ? "" : "LEFT_THUMB ");
			printf("%s", (state->buttons & CONTROLLER_BUTTON_DPAD_UP) == 0 ? "" : "DPAD_UP ");
			printf("%s", (state->buttons & CONTROLLER_BUTTON_DPAD_DOWN) == 0 ? "" : "DPAD_DOWN ");
			printf("%s", (state->buttons & CONTROLLER_BUTTON_DPAD_LEFT) == 0 ? "" : "DPAD_LEFT ");
			printf("%s", (state->buttons & CONTROLLER_BUTTON_DPAD_RIGHT) == 0 ? "" : "DPAD_RIGHT ");
			printf("\n");
			printf("triggler  %.4f\n", state->axes[CONTROLLER_AXIS_LEFT_TRIGGER]);
			printf("touchPad  %.4f, %4f\n", state->axes[CONTROLLER_AXIS_LEFT_THUMB_X],state->axes[CONTROLLER_AXIS_LEFT_THUMB_Y]);
			preTimestamp = state->timestamp;
		}

		ch = getchar();
		if (ch == 'r' || ch == 'R') {
			XDeviceSendMessage(handle, kMessage_RecenterSensor, 0, 0);
		}else if (ch == 'b' || ch == 'B') {
			printf("\n device  battery level %d\n\n", XDeviceGetInt(handle,kField_BatteryLevel,0));
		}


	} while (ch != 'c' && ch != 'C');

	XDeviceExit();
	free(state);
}

int main()
{
	char ch;
	do
	{
		switch (menu())
		{
			case 1: func001(); break;
			case 2: func002(); break;
			default:break;
		}

		do{
			printf("Do you want to continue to operate？(y/n)");
			ch = get_first();
		} while (ch != 'y' && ch != 'Y' && ch != 'n' && ch != 'N');
	} while (ch == 'y' || ch == 'Y');

	return 0;
}

