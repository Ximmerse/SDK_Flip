#ifndef __XIM_FIELD_ID_H__
#define __XIM_FIELD_ID_H__

enum FieldID/*:int*/{
	// Bool
	kField_BoolOffset=0,
	kField_CtxCanProcessInputEvent=kField_BoolOffset,
	kField_CtxBoolMax,
	kField_AutoProcessInputEvent=kField_BoolOffset,
	kField_CanProcessInputEvent,
	kField_CanProcessAxisEvent,
	kField_CanProcessKeyEvent,
	kField_CanProcessPositionEvent,
	kField_CanProcessAccelerometerEvent,
	kField_CanProcessRotationEvent,
	kField_CanProcessGyroscopeEvent,
	kField_CanCheckAxesRange,
	kField_CanMapAxesToButtons,
	kField_IsAbsRotation,
	kField_BoolMax,
	// Int
	kField_IntOffset=0,
	kField_CtxSdkVersion=kField_IntOffset,
	kField_CtxDeviceVersion,
	kField_CtxPlatformVersion,
	kField_CtxIntMax,
	kField_NumAxes=kField_IntOffset,
	kField_NumButtons,
	kField_ErrorCode,
	kField_ConnectionState,
	kField_BatteryLevel,
	kField_TrackingResult,
	kField_TrackingOrigin,
	kField_IntMax,
	// Float
	kField_FloatOffset=0,
	kField_PositionScale=kField_FloatOffset,
	kField_TrackerHeight,
	kField_TrackerDepth,
	kField_TrackerPitch,
	kField_TriggerAsButtonThreshold=kField_FloatOffset,
	kField_AxisAsButtonThreshold,
	kField_AxisDeadzoneThreshold,
	kField_AccelScale,
	kField_GyroScale,
	kField_FloatMax,
	// Object
	kField_ObjectOffset=0,
	kField_TRS=kField_ObjectOffset,
	kField_ObjectMax,
};

#endif