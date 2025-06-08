# sort.py
from filterpy.kalman import KalmanFilter
import numpy as np

class Track:
    count = 0

    def __init__(self, bbox):
        self.kf = KalmanFilter(dim_x=7, dim_z=4)
        self.kf.F = np.array([[1,0,0,0,1,0,0],
                              [0,1,0,0,0,1,0],
                              [0,0,1,0,0,0,1],
                              [0,0,0,1,0,0,0],
                              [0,0,0,0,1,0,0],
                              [0,0,0,0,0,1,0],
                              [0,0,0,0,0,0,1]])
        self.kf.H = np.array([[1,0,0,0,0,0,0],
                              [0,1,0,0,0,0,0],
                              [0,0,1,0,0,0,0],
                              [0,0,0,1,0,0,0]])
        self.kf.R *= 10
        self.kf.P *= 10
        self.kf.Q *= 0.01
        self.kf.x[:4] = np.array(bbox).reshape((4,1))
        self.id = Track.count
        Track.count += 1
        self.hits = 0
        self.no_losses = 0

    def predict(self):
        self.kf.predict()

    def update(self, bbox):
        self.kf.update(np.array(bbox).reshape((4, 1)))
        self.hits += 1

class Sort:
    def __init__(self):
        self.tracks = []

    def update(self, detections):
        updated_tracks = []
        for track in self.tracks:
            track.predict()
        
        for det in detections:
            assigned = False
            for track in self.tracks:
                track.update(det)
                updated_tracks.append(track)
                assigned = True
                break

            if not assigned:
                updated_tracks.append(Track(det))
        
        self.tracks = updated_tracks
        return self.tracks
