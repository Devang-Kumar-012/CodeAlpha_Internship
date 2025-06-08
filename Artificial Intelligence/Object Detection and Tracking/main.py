import cv2
from ultralytics import YOLO
from sort import Sort

# Load YOLOv8 model
model = YOLO("yolov8n.pt")  # You can try yolov8s.pt for better accuracy
tracker = Sort()

# Start webcam
cap = cv2.VideoCapture(0)

while True:
    ret, frame = cap.read()
    if not ret:
        break

    # Run detection
    results = model(frame)[0]

    detections = []
    for box in results.boxes:
        x1, y1, x2, y2 = map(int, box.xyxy[0])
        detections.append([x1, y1, x2, y2])

    # Update tracker
    tracks = tracker.update(detections)

    # Draw tracks
    for track in tracks:
        x1, y1, x2, y2 = map(int, track.kf.x[:4])
        track_id = track.id
        cv2.rectangle(frame, (x1, y1), (x2, y2), (255, 0, 0), 2)
        cv2.putText(frame, f"ID {track_id}", (x1, y1 - 10),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 255, 255), 2)

    # Show output
    cv2.imshow("Object Detection & Tracking", frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
