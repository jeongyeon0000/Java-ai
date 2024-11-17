package yolo;

import org.opencv.core.*;
import org.opencv.dnn.Net;
import org.opencv.dnn.Dnn;
import org.opencv.videoio.VideoCapture;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Aiopencv {
    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) {
        String modelConfiguration = "C:\\data\\AI\\yolov4.cfg";
        String modelWeights = "C:\\data\\AI\\yolov4.weights";

        Net net = Dnn.readNetFromDarknet(modelConfiguration, modelWeights);
        net.setPreferableBackend(Dnn.DNN_BACKEND_OPENCV);
        net.setPreferableTarget(Dnn.DNN_TARGET_CPU);

        VideoCapture capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            System.out.println("Error: Could not open video capture.");
            return;
        }

        Mat frame = new Mat();
        List<String> classNames = List.of("person", "bicycle", "car", "motorbike", "aeroplane", "bus", "train", "truck", "boat",
                "traffic light", "fire hydrant", "stop sign", "parking meter", "bench",
                "bird", "cat", "dog", "horse", "sheep", "cow", "elephant", "bear",
                "zebra", "giraffe", "backpack", "umbrella", "handbag", "tie", "suitcase",
                "frisbee", "skis", "snowboard", "sports ball", "kite", "baseball bat",
                "baseball glove", "skateboard", "surfboard", "tennis racket", "bottle",
                "wine glass", "cup", "fork", "knife", "spoon", "bowl", "banana", "apple",
                "sandwich", "orange", "broccoli", "carrot", "hot dog", "pizza", "donut",
                "cake", "chair", "couch", "potted plant", "bed", "dining table", "toilet",
                "TV", "laptop", "mouse", "remote", "keyboard", "cell phone", "microwave",
                "oven", "toaster", "sink", "refrigerator", "book", "clock", "vase",
                "scissors", "teddy bear", "hair drier", "toothbrush");

        while (capture.read(frame)) {
            Mat blob = Dnn.blobFromImage(frame, 1 / 255.0, new Size(416, 416), new Scalar(0, 0, 0), true, false);
            net.setInput(blob);

            List<Mat> result = new ArrayList<>();
            net.forward(result, net.getUnconnectedOutLayersNames());

            List<Rect2d> boxes = new ArrayList<>();
            List<Float> confidences = new ArrayList<>();

            for (Mat detection : result) {
                for (int i = 0; i < detection.rows(); i++) {
                    float confidence = (float) detection.get(i, 4)[0];
                    if (confidence > 0.5) {
                        int classId = (int) detection.get(i, 5)[0];
                        if (classId == 0) {
                            int x = (int) (detection.get(i, 0)[0] * frame.cols());
                            int y = (int) (detection.get(i, 1)[0] * frame.rows());
                            int width = (int) (detection.get(i, 2)[0] * frame.cols());
                            int height = (int) (detection.get(i, 3)[0] * frame.rows());

                            boxes.add(new Rect2d(x, y, width, height));
                            confidences.add(confidence);
                        }
                    }
                }
            }

            // Convert confidences list to MatOfFloat
            MatOfFloat confidencesMat = new MatOfFloat();
            confidencesMat.fromList(confidences);

            MatOfRect2d boxesMat = new MatOfRect2d();
            boxesMat.fromList(boxes);
            MatOfInt indices = new MatOfInt();
            Dnn.NMSBoxes(boxesMat, confidencesMat, 0.5f, 0.4f, indices);

            int personCount = indices.rows();
            System.out.println("Number of people detected: " + personCount);

            Imgproc.putText(frame, "People Count: " + personCount, new Point(10, 30),
                    Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 2);

            for (int i = 0; i < indices.rows(); i++) {
                int idx = (int) indices.get(i, 0)[0];
                Rect2d box = boxes.get(idx);
                Imgproc.rectangle(frame, new Point(box.x, box.y), new Point(box.x + box.width, box.y + box.height), new Scalar(0, 255, 0), 2);
            }

            HighGui.imshow("YOLO Object Detection", frame);
            if (HighGui.waitKey(1) == 27) {
                break;
            }
        }

        capture.release();
        HighGui.destroyAllWindows();
    }
}