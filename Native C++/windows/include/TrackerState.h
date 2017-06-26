#ifndef __XIM_TRACKER_STATE_H__
#define __XIM_TRACKER_STATE_H__

enum TrackingResult{
	kTrackingResult_NotTracked      =    0,
	kTrackingResult_RotationTracked = 1<<0,
	kTrackingResult_PositionTracked = 1<<1,
	kTrackingResult_PoseTracked     = (kTrackingResult_RotationTracked|kTrackingResult_PositionTracked),
};

enum TrackingOrigin{
	kTrackingOrigin_Unknown    =-1,
	kTrackingOrigin_EyeLevel   = 0,
	kTrackingOrigin_FloorLevel = 1,
};

typedef struct tagTrackerState{
	int handle;
	int timestamp;
	int frameCount;
	int capacity;
	int count;
	int* id;
	float* data;
}TrackerState;

#endif