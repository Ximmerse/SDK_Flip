#ifndef __XIM_INPUT_DEVICE_H__
#define __XIM_INPUT_DEVICE_H__

#include "Delegates.h"

enum DeviceType {
	kDeviceType_Unknown,
	kDeviceType_ControllerDevice,
	kDeviceType_TrackerDevice,
};

enum ConnectionState {
	kConnectionState_Disconnected,
	kConnectionState_Scanning,
	kConnectionState_Connecting,
	kConnectionState_Connected,
	kConnectionState_Error,
};

class DeviceContext;

class InputDevice {

public:
	InputDevice();
	virtual ~InputDevice();

public:
	DeviceContext* context;
	int handle;
	char* name;
	int type;

	bool* boolValues;
	int* intValues;
	float* floatValues;
	void** objectValues;

	int* objectSizes;

public:
	void_delegate onUpdateInputState;
	void_delegate onProcessInputEvent;

	axis_delegate onProcessAxisEvent;
	key_delegate onProcessKeyEvent;

	vector3f_delegate onProcessPositionEvent;
	vector4f_delegate onProcessRotationEvent;

public:
	virtual void GetInputState(void* state);
	virtual bool SendMessage_(int Msg, int wParam, int lParam);

	virtual void ResetInputState();
	virtual void UpdateInputState();
	virtual void UpdateInputBuffer(unsigned char* buffer, int offset, int count);
	
	virtual void SerializeMessage(unsigned char* buffer, int offset, int count,
		                          int Msg, int wParam, int lParam);
	virtual void SendBuffer(unsigned char* buffer, int offset, int count);

	virtual void SetEventListener(int eventID, void* listener);
	virtual void DispatchEventListener(int eventID);
	
	virtual void ProcessInputEvent();
	virtual void ProcessAxisEvent(int axis, float value);
	virtual void ProcessKeyEvent(int code, int action);

	virtual void ProcessPositionEvent(int node, float x, float y, float z);
	virtual void ProcessRotationEvent(int node, float x, float y, float z, float w);

public:
	virtual bool GetBool(int fieldID, bool defaultValue);
	virtual void SetBool(int fieldID, bool value);
	virtual int GetInt(int fieldID, int defaultValue);
	virtual void SetInt(int fieldID, int value);
	virtual float GetFloat(int fieldID, float defaultValue);
	virtual void SetFloat(int fieldID, float value);
	virtual void GetObject_(int fieldID, unsigned char* buffer, int offset, int count);
	virtual void SetObject(int fieldID, unsigned char* buffer, int offset, int count);
};

#endif