#ifndef __XIM_DELEGATES_H__
#define __XIM_DELEGATES_H__

#include "ControllerState.h"

typedef void(*void_delegate)(int which);
typedef void(*axis_delegate)(int which, int axis, float value);
typedef void(*key_delegate)(int which, int code, int action);
typedef void(*vector3f_delegate)(int which, int node, float x, float y, float z);
typedef void(*vector4f_delegate)(int which, int node, float x, float y, float z, float w);

typedef int(*controller_state_delegate)(int which, ControllerState* state);
typedef int(*send_message_delegate)(int which, int Msg, int wParam, int lParam);

#endif