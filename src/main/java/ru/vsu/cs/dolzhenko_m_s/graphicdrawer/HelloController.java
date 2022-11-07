package ru.vsu.cs.dolzhenko_m_s.graphicdrawer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import ru.vsu.cs.dolzhenko_m_s.graphicdrawer.domain.*;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final ScreenConverter screenConverter = new ScreenConverter(-1, 1, 2, 2, WIDTH, HEIGHT);

    private ScreenPoint startPoint = null;

    @FXML
    private TextField functionField;

    @FXML
    private Canvas drawingCanvas = new Canvas(HEIGHT, WIDTH);

    private GraphicsContext graphicsContext;

    private int currentScroll = 0;

    private double delta = 0;

    private double scale = 1;

    private Function function;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphicsContext = drawingCanvas.getGraphicsContext2D();
        repaint();
    }

    @FXML
    private void onScroll(ScrollEvent e) {
        int clicks = -(int) e.getTextDeltaY() / 2;
        currentScroll += clicks;
        scale = getScale();
        screenConverter.changeScale(scale);
        //screenConverter.moveCorner(new RealPoint(0,0));

        repaint();
    }

    private void repaint() {
        paint(graphicsContext);
    }

    @FXML
    private void onMousePressed(MouseEvent e) {
        startPoint = new ScreenPoint((int) e.getX(), (int) e.getY());
    }

    @FXML
    private void onMouseDragged(MouseEvent e) {
        ScreenPoint newPoint = new ScreenPoint((int) e.getX(), (int) e.getY());
        RealPoint p1 = screenConverter.screenPointToReal(newPoint);
        RealPoint p2 = screenConverter.screenPointToReal(startPoint);

        RealPoint deltaPoint = p2.minus(p1);
        screenConverter.moveCorner(deltaPoint);
        delta = (int) (Math.sqrt(Math.pow(screenConverter.getCenterX(), 2) + Math.pow(screenConverter.getCenterY(), 2)) * 15);

        startPoint = newPoint;
        repaint();
    }

    protected void paint(GraphicsContext graphicsContext) {
        screenConverter.setScreenHeight(HEIGHT);
        screenConverter.setScreenWidth(WIDTH);
        graphicsContext.clearRect(0, 0, WIDTH, HEIGHT);

        //scale = getScale();

        drawDashes(graphicsContext);

        if (function != null)
            drawFunction(graphicsContext);

        drawAsixs(graphicsContext);
    }

    private void drawAsixs(GraphicsContext graphicsContext) {
        Line oX = new Line(new RealPoint(-1 - delta, 0), new RealPoint(1 + delta, 0));
        Line oY = new Line(new RealPoint(0, -1 - delta), new RealPoint(0, 1 + delta));
        drawLine(graphicsContext, oX);
        drawLine(graphicsContext, oY);
    }

    private void drawFunction(GraphicsContext graphicsContext) {
        RealPoint prev = null;
        for (double i = 0; i <= WIDTH; i += 0.5) {
            double realY = 0;
            try {
                double realX = screenConverter.getRealXFromScreen(i);
                realY = function.compute(realX);
                var current = new RealPoint(realX, realY);
                if (prev != null)
                    drawLine(graphicsContext, new Line(prev, current));
                prev = current;
            } catch (Exception e) {
                System.out.println("exception thrown" + " formula =  " + functionField.getText() + " x is " + i);
                prev = null;
            }
        }
    }

    private void drawDashes(GraphicsContext graphicsContext) {
        for (BigDecimal i = new BigDecimal(0); i.compareTo(BigDecimal.valueOf(screenConverter.getRealWidth() / 2 + delta)) <= 0; i = i.add(BigDecimal.valueOf(screenConverter.getRealWidth() / 20))) {
            RealPoint upX = new RealPoint(i.doubleValue(), 0 + 0.02);
            RealPoint downX = new RealPoint(i.doubleValue(), 0 - 0.02);
            drawLine(graphicsContext, new Line(upX, downX));

            RealPoint textXpoint = new RealPoint(i.doubleValue(), 0 - 0.04);
            var screenPointX = screenConverter.realPointToScreen(textXpoint);
            graphicsContext.fillText("" + i, screenPointX.getX(), screenPointX.getY());


            RealPoint upY = new RealPoint(0 + 0.02, i.doubleValue());
            RealPoint downY = new RealPoint(0 - 0.02, i.doubleValue());
            drawLine(graphicsContext, new Line(upY, downY));

            if (i.doubleValue() != 0) {
                RealPoint textYpoint = new RealPoint(0 + 0.04, i.doubleValue());
                var screenPointY = screenConverter.realPointToScreen(textYpoint);
                graphicsContext.fillText("" + i, screenPointY.getX(), screenPointY.getY());
            }
        }

        for (BigDecimal i = BigDecimal.valueOf(-1 * screenConverter.getRealWidth() / 20); i.compareTo(BigDecimal.valueOf(-1 * screenConverter.getRealWidth() / 2 - delta)) >= 0; i = i.add(BigDecimal.valueOf(-1 * screenConverter.getRealWidth() / 20))) {
            RealPoint upX = new RealPoint(i.doubleValue(), 0 + 0.02);
            RealPoint downX = new RealPoint(i.doubleValue(), 0 - 0.02);
            drawLine(graphicsContext, new Line(upX, downX));

            RealPoint textXpoint = new RealPoint(i.doubleValue(), 0 - 0.04);
            var screenPointX = screenConverter.realPointToScreen(textXpoint);
            graphicsContext.fillText("" + i, screenPointX.getX(), screenPointX.getY());


            RealPoint upY = new RealPoint(0 + 0.02, i.doubleValue());
            RealPoint downY = new RealPoint(0 - 0.02, i.doubleValue());
            drawLine(graphicsContext, new Line(upY, downY));

            if (i.doubleValue() != 0) {
                RealPoint textYpoint = new RealPoint(0 + 0.04, i.doubleValue());
                var screenPointY = screenConverter.realPointToScreen(textYpoint);
                graphicsContext.fillText("" + i, screenPointY.getX(), screenPointY.getY());
            }
        }
    }

    private void drawLine(GraphicsContext g, Line line) {
        ScreenPoint p1 = screenConverter.realPointToScreen(line.getStartPoint());
        ScreenPoint p2 = screenConverter.realPointToScreen(line.getEndPoint());

        g.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    @FXML
    private void computeFunction(ActionEvent actionEvent) {
        if (functionField.getText() != null) {
            function = new MatchParser(functionField.getText());
            repaint();
        }
    }

    private double getScale() {
        int[] scales = new int[]{1,2,5};
        int clicks = Math.abs(currentScroll);

        double newScale = scales[clicks % 3];
        double delta = 0.1d;

        if(currentScroll == 0) {
            return 1;
        }

        if(currentScroll > 0) {
            delta = 10;
        }
        clicks = clicks / 3;
        while(clicks > 0) {
            newScale *= delta;
            clicks--;
        }

        return newScale;
    }
}