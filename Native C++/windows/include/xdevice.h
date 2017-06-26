#ifndef __XIM_XDEVICE_H__
#define __XIM_XDEVICE_H__

#include "Delegates.h"
#include "FieldIDS.h"
#include "TrackerState.h"
#include "InputDevice.h"
#include "MessageIDs.h"

#ifndef XIM_API
#if defined(_WIN32)||defined(_WIN64)
	#define XIM_API __declspec(dllimport)
#else
	#define XIM_API
#endif
#endif

// Device ID

#define ID_CONTEXT -1

#define XIM_DK3 0x3000
#define XIM_DK4 0x4000

class DeviceContext;
class InputDevice;

#if defined(__LANGUAGE_C_PLUS_PLUS)||defined(__cplusplus)||defined(c_plusplus)
extern "C" {
#endif
	// Common
	/** Initialize the X-Device SDK library.*/
	XIM_API int XDeviceInit(void);
	/** Finalize the X-Device SDK library.*/
	XIM_API int XDeviceExit(void);

	// Device Management

	/** Get the current device context of X-Device SDK.If autoCreate is true,it will ensure the context is created.*/
	XIM_API DeviceContext* XDeviceGetContext(bool autoCreate);
	/** Get the pointer of input device identified by handle.*/
	XIM_API InputDevice* XDeviceGetInputDevice(int which);
	/** Get the handle of input device identified by name.*/
	XIM_API int XDeviceGetInputDeviceHandle(char* name);
	/** Get the name of input device identified by handle.*/
	XIM_API char* XDeviceGetInputDeviceName(int which);

	// I/O

	/** Get the input state of input device identified by handle.*/
	XIM_API int XDeviceGetInputState(int which, void* state);
	/** Send a message to the input device identified by handle.*/
	XIM_API int XDeviceSendMessage(int which, int Msg, int wParam, int lParam);
	
	/** Reset the input state of input device identified by handle.*/
	XIM_API int XDeviceResetInputState(int which);
	/** Update the input device identified by handle.*/
	XIM_API int XDeviceUpdateInputState(int which);

	// Event

	/** Set an event listener on the event identified by eventID of the input device identified by handle.*/
	XIM_API int XDeviceSetEventListener(int which, int eventID, void* listener);
	/** Dispatch the event identified by eventID of the input device identified by handle.*/
	XIM_API int XDeviceDispatchEventListener(int which, int eventID);

	// Get/Set

	/** Get a bool value of input device identified by handle.*/
	XIM_API bool XDeviceGetBool(int which, int fieldID, bool defaultValue);
	/** Set a bool value of input device identified by handle.*/
	XIM_API void XDeviceSetBool(int which, int fieldID, bool value);
	/** Get an int value of input device identified by handle.*/
	XIM_API int XDeviceGetInt(int which, int fieldID, int defaultValue);
	/** Set an int value of input device identified by handle.*/
	XIM_API void XDeviceSetInt(int which, int fieldID, int value);
	/** Get a float value of input device identified by handle.*/
	XIM_API float XDeviceGetFloat(int which, int fieldID, float defaultValue);
	/** Set a float value of input device identified by handle.*/
	XIM_API void XDeviceSetFloat(int which, int fieldID, float value);
	/** Get a object value of input device identified by handle.*/
	XIM_API void XDeviceGetObject(int which, int fieldID, unsigned char* buffer, int offset, int count);
	/** Set a object value of input device identified by handle.*/
	XIM_API void XDeviceSetObject(int which, int fieldID, unsigned char* buffer, int offset, int count);

	  //Ext

	/** */
	XIM_API int XDeviceGetTickCount();

#if defined(__LANGUAGE_C_PLUS_PLUS)||defined(__cplusplus)||defined(c_plusplus)
}
#endif

#endif