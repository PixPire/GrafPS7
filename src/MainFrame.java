import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.io.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    boolean theOtherWay=false;
    boolean firstClick=true;
    boolean secondClick=false;
    boolean thirdClick=false;
    int mode=0;//1 -> spoczynek, 2-> przesuwanie, 3->Obracanie, 4-> Skalowanie
    Point selectedPoint;
    int lastX, lastY;
    int heightDiff = 100;
    PolygonFigure selectedFigure=new PolygonFigure();
    ArrayList<PolygonFigure> figuresList = new ArrayList<>();
    int selectedPointIndex=-1;
    JDialog dialog = new JDialog();
    MouseListener mouseListener;
    CanvasPanel canvasPanel=new CanvasPanel();
    int currentMouseX, currentMouseY;

    boolean settingPointsMode = false;
    int x1,y1;


    int maxPointsNumber,currentPointsNumber;


    JPanel toolsPanel = new JPanel();

    public MainFrame(){
        setSize(2000,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Przekształcenia 2D");
        Container content = getContentPane();
        content.setLayout(new BorderLayout());

        content.add(canvasPanel, BorderLayout.CENTER);

        initToolsPanel();

        content.add(toolsPanel, BorderLayout.NORTH);

        initMouseEditTool();

        setVisible(true);



      //  add(canvasPanel);
    }

    private void initMouseEditTool() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(mode==1){
                    if(firstClick) {
                        SearchForFigure(e.getX(), e.getY() - heightDiff);
                        if(selectedFigure!=null)firstClick=false;
                    }else{
                        firstClick=true;
                        selectedFigure=null;
                    }
                }

                if(mode==2){
                    if(firstClick) {
                        SearchForFigure(e.getX(), e.getY() - heightDiff);
                        if(selectedFigure!=null){
                            firstClick=false;
                            secondClick=true;
                        }
                    }else if(secondClick){
                        selectedPoint=new Point(e.getX(), e.getY());
                        secondClick=false;
                        thirdClick=true;
                        System.out.println("Ustalono Punkt");

                    }else{
                        thirdClick=false;
                        firstClick=true;
                        selectedFigure=null;
                        selectedPoint=null;
                    }



                }


            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(!settingPointsMode) {

                    //selectPointId(new Point(currentMouseX, currentMouseY));

                }else System.out.println("nie wyszukuje punktu -> nie skończono ustawiać");

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                canvasPanel.setFigureSet(figuresList);
                canvasPanel.repaint();
                selectedPointIndex=-1;
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }





    private void initToolsPanel() {
        toolsPanel.setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        toolsPanel.add(toolBar, BorderLayout.PAGE_START);
        JButton confirmPointAmountButton = new JButton("Potwierdź Ilość kątów");
        Dimension dimension = new Dimension(50,100);
        JTextField pointsNumber = new JTextField("5");
        pointsNumber.setColumns(2);
        pointsNumber.setMaximumSize(dimension);
        toolBar.add(confirmPointAmountButton);
        toolBar.add(pointsNumber);

        JLabel mousePositionInfoLabel = new JLabel(" Pozycja myszki");
        JLabel mousePositionXLabel = new JLabel(" X: 100 ");
        JLabel mousePositionYLabel = new JLabel(" Y: 200 ");
        mousePositionChecker(mousePositionXLabel, mousePositionYLabel);

        toolBar.add(mousePositionInfoLabel);
        toolBar.add(mousePositionXLabel);
        toolBar.add(mousePositionYLabel);

        JButton confirmPointPositionButton = new JButton("Potwierdź kąt:");
        toolBar.add(confirmPointPositionButton);

        JTextField newPointXField = new JTextField("100");
        JTextField newPointYField = new JTextField("100");
        newPointXField.setMaximumSize(dimension);
        newPointYField.setMaximumSize(dimension);

        toolBar.add(newPointXField);
        toolBar.add(newPointYField);

        confirmPointAmountButton.addActionListener(e -> ConfirmedPointsAmount(Integer.parseInt(pointsNumber.getText())));
        confirmPointPositionButton.addActionListener(e-> ConfirmedNewPointPosition(Integer.parseInt(newPointXField.getText()),Integer.parseInt(newPointYField.getText())));

        JButton editPositionButton = new JButton("Przesuń o wektor");
        toolBar.add(editPositionButton);

        JTextField editPointNumberField = new JTextField("1");
        JTextField editPointXField = new JTextField("100");
        JTextField editPointYField = new JTextField("100");




        editPointNumberField.setMaximumSize(dimension);
        editPointXField.setMaximumSize(dimension);
        editPointYField.setMaximumSize(dimension);

        editPositionButton.addActionListener(e -> editFigurePositionUsingTextBox(Integer.parseInt(editPointNumberField.getText()), Integer.parseInt(editPointXField.getText()), Integer.parseInt(editPointYField.getText())));



        toolBar.add(editPointNumberField);
        toolBar.add(editPointXField);
        toolBar.add(editPointYField);

        JButton rotateButton = new JButton("Obrót względem punktu");
        JTextField rotateNumberField = new JTextField("1");
        JTextField rotateAngleField = new JTextField("360");
        JTextField rotatePointXField = new JTextField("100");
        JTextField rotatePointYField = new JTextField("100");
        rotateNumberField.setMaximumSize(dimension);
        rotateAngleField.setMaximumSize(dimension);
        rotatePointXField.setMaximumSize(dimension);
        rotatePointYField.setMaximumSize(dimension);
        rotateButton.addActionListener(e->rotateFigure(Integer.parseInt(rotateNumberField.getText()),Integer.parseInt(rotateAngleField.getText()),Integer.parseInt(rotatePointXField.getText()),Integer.parseInt(rotatePointYField.getText()) ));


        toolBar.add(rotateButton);
        toolBar.add(rotateNumberField);
        toolBar.add(rotateAngleField);
        toolBar.add(rotatePointXField);
        toolBar.add(rotatePointYField);

        JButton scaleButton = new JButton("Skalowanie względem punktu");
        JTextField scaleNumberField = new JTextField("1");
        JTextField scalePointXField = new JTextField("100");
        JTextField scalePointYField = new JTextField("100");
        scaleNumberField.setMaximumSize(dimension);
        scalePointXField.setMaximumSize(dimension);
        scalePointYField.setMaximumSize(dimension);
        toolBar.add(scaleButton);
        toolBar.add(scaleNumberField);
        toolBar.add(scalePointXField);
        toolBar.add(scalePointYField);

        JLabel modeInstruction = new JLabel(" Zmiana Trybu ->");
        JButton modeButton = new JButton("Tryb Tekstowy");
        toolBar.add(modeInstruction);
        toolBar.add(modeButton);
        modeButton.addActionListener(e->ChangeMode(modeButton));

        JButton saveFiguresButton = new JButton("Zapisz");
        JButton loadFiguresButton = new JButton("Wczytaj");

        toolBar.add(saveFiguresButton);
        toolBar.add(loadFiguresButton);
        
        saveFiguresButton.addActionListener(e->saveFigures());
        loadFiguresButton.addActionListener(e->loadFigures());
        JButton resetButton = new JButton("Reset");
        toolBar.add(resetButton);
        resetButton.addActionListener(e->ResetFigureSet());


    }

    private void rotateFigure(int figureIndex, int rotateAngle, int xValue, int yValue) {
        yValue+=68;
        PolygonFigure tempFig=figuresList.get(figureIndex-1);
        for (Point point:tempFig.pointsList
             ) {
                int startX=point.x;
                int startY=point.y;
                point.x= (int) ( xValue+(startX-xValue)*Math.cos((double)rotateAngle/57)-(startY-yValue)*Math.sin((double)rotateAngle/57));
                point.y= (int) ( yValue+(startX-xValue)*Math.sin((double)rotateAngle/57)+(startY-yValue)*Math.cos((double)rotateAngle/57));
        }

        canvasPanel.setFigureSet(figuresList);
        canvasPanel.PaintFigures();

    }

    private void ResetFigureSet() {
        figuresList=new ArrayList<>();
        canvasPanel.setFigureSet(figuresList);
    }


    private void ChangeMode(JButton modeButton) {
        switch (mode) {
            case 0 -> {
                modeButton.setText("Tryb Przesuwania");//mode 1 = Tryb Przesuwania
                mode = 1;
                selectedPoint=null;
                selectedFigure=null;
                firstClick=true;
            }
            case 1 -> {
                modeButton.setText("Tryb Obracania");//mode 2 = Tryb Obracania
                mode = 2;
                selectedPoint=null;
                selectedFigure=null;
                firstClick=true;
            }
            case 2 -> {
                modeButton.setText("Tryb Skalowania");//mode 3 = Tryb Skalowania
                mode = 3;
                selectedPoint=null;
                selectedFigure=null;
                firstClick=true;
            }
            case 3 -> {
                modeButton.setText("Tryb Tekstowy");//mode 0 = Tryb Tekstowy
                mode = 0;
                selectedPoint=null;
                selectedFigure=null;
                firstClick=true;
            }
        }
    }


    private void editFigurePositionUsingTextBox(int figureIndex, int vectorX, int vectorY) {

        System.out.println("Tutaj będzie zmiana pozycji używając textfielda");

        selectedFigure=figuresList.get(figureIndex-1);
        for (Point point:selectedFigure.pointsList
             ) {point.move(point.x+vectorX,point.y+vectorY);

        }
        figuresList.set(figureIndex-1, selectedFigure);

        canvasPanel.setFigureSet(figuresList);


    }


    private void ConfirmedNewPointPosition(int x, int y) {

        System.out.println("Potwierdzono przyciskiem punkt X:  " + x + " Y: "+y);
        if (currentPointsNumber <= maxPointsNumber) {
            if (currentPointsNumber == 1) {


                selectedFigure.pointsList.add(new Point(x,y));
                System.out.println("Zapisano X1 i Y1");

                figuresList.add(selectedFigure);
                canvasPanel.setFigureSet(figuresList);


            } else {
                System.out.println("Zapisano Kolejny X i Y");
                System.out.println("Rysuję Prostą");
                selectedFigure.addPoint(new Point(x,y));


                canvasPanel.setFigureSet(figuresList);
                canvasPanel.PaintFigures();
            }
            System.out.println("Narysowano łącznie " + String.valueOf(currentPointsNumber) + " punktów");
            if (currentPointsNumber != maxPointsNumber)
                System.out.println("Przechodzę do punktu " + String.valueOf(currentPointsNumber + 1));
            currentPointsNumber++;

            if (currentPointsNumber == maxPointsNumber) settingPointsMode = false;
        }
    }

    private void SearchForFigure(int x, int y){
        y=y+68;
        for (PolygonFigure figure:figuresList
        ) {
            System.out.println("Sprawdzam figurę");
            if(figure.pointsList.size()>1) {
                System.out.println("Wewnątrz Figury");
                for (int i = 0; i < figure.pointsList.size()-1; i++) {
                    Line2D line = new Line2D.Double();
                   line.setLine(figure.pointsList.get(i).x, figure.pointsList.get(i).y, figure.pointsList.get(i + 1).x, figure.pointsList.get(i + 1).y);
                   if(line.ptSegDist(x,y)<=40){
                    selectedFigure=figure;
                    System.out.println("Znaleziono!");
                    return;
                   }
                }
                Line2D line = new Line2D.Double();
                if(figure.pointsList.size()==figure.pointsAmount)line.setLine(figure.pointsList.get(figure.pointsList.size()-1).x, figure.pointsList.get(figure.pointsList.size()-1).y, figure.pointsList.get(0).x, figure.pointsList.get(0).y);
                if(line.ptSegDist(x,y)<=5){
                    selectedFigure=figure;
                    System.out.println("Znaleziono!");
                    return;
                }
            }

        }
    }
    private void ConfirmedPointsAmount(int pointsNumber) {

        removeMouseListener(mouseListener);


        mouseListener=null;
        settingPointsMode=true;

        selectedFigure=new PolygonFigure();
        selectedFigure.setPointsAmount(pointsNumber);

        System.out.println("Zaczynam Rysować Wielokąt o "+pointsNumber+" kątach");


        maxPointsNumber=pointsNumber;
        currentPointsNumber=1;



        addMouseListener(mouseListener=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(currentPointsNumber<=maxPointsNumber) {
                    if (currentPointsNumber==1) {
                        System.out.println("Zapisano X1 i Y1");
                        x1 = e.getX();
                        y1 = e.getY();


                        selectedFigure.pointsList.add(new Point(x1,y1));
                        System.out.println("Zapisano X1 i Y1");

                        figuresList.add(selectedFigure);
                        canvasPanel.setFigureSet(figuresList);

                    } else {
                        System.out.println("Zapisano Kolejny X i Y");
                        System.out.println("Rysuję Prostą");
                        selectedFigure.addPoint(new Point(e.getX(),e.getY()));
                        figuresList.set(figuresList.size()-1,selectedFigure);

                        canvasPanel.setFigureSet(figuresList);
                        canvasPanel.PaintFigures();
                    }
                    System.out.println("Narysowano łącznie "+String.valueOf(currentPointsNumber)+" punktów");
                    if(currentPointsNumber!=maxPointsNumber)System.out.println("Przechodzę do punktu "+String.valueOf(currentPointsNumber+1));
                    currentPointsNumber++;
                    if(currentPointsNumber==maxPointsNumber)settingPointsMode=false;

                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void mousePositionChecker(JLabel xLabel, JLabel yLabel){
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

                currentMouseX =e.getX();
                currentMouseY =e.getY();

            }

            @Override
            public void mouseMoved(MouseEvent e) {



                lastX=currentMouseX;
                lastY=currentMouseY;
                currentMouseX=e.getX();
                xLabel.setText("X: "+currentMouseX);
                currentMouseY=e.getY();
                yLabel.setText("Y: "+currentMouseY);
                int differenceX=lastX-currentMouseX;
                int differenceY=lastY-currentMouseY;




                if(mode==1&&selectedFigure!=null){
                    System.out.println("EDYTUJĘ W CZASIE RZECZYWISTYM");
                    int vectorX, vectorY;
                    vectorX=selectedFigure.pointsList.get(0).x-differenceX;
                    vectorY=selectedFigure.pointsList.get(0).y-differenceY;
                    System.out.println("Pozycja X: "+vectorX);
                    System.out.println("Pozycja Y: "+vectorY);
                    PolygonFigure copiedFigure=selectedFigure;
                    copiedFigure.moveByVector(-differenceX, -differenceY);

                    figuresList.set(figuresList.indexOf(selectedFigure),copiedFigure );


                    canvasPanel.setFigureSet(figuresList);
                    canvasPanel.PaintFigures();


                }


                if(mode==2&&selectedFigure!=null&&selectedPoint!=null){
                    System.out.println("OBRACAM W CZASIE RZECZYWISTYM");
                    int vectorX, vectorY;
                    vectorX=selectedFigure.pointsList.get(0).x-differenceX;
                    vectorY=selectedFigure.pointsList.get(0).y-differenceY;
                    System.out.println("Różnica X: "+vectorX);
                    System.out.println("Różnica Y: "+vectorY);

                    int totalDifference=vectorX+vectorY;
                    totalDifference=totalDifference%5;


                    if(differenceX>differenceY)rotateFigure(figuresList.indexOf(selectedFigure)+1, -totalDifference, selectedPoint.x, selectedPoint.y);
                    else rotateFigure(figuresList.indexOf(selectedFigure)+1, -totalDifference, selectedPoint.x, selectedPoint.y);

                    canvasPanel.setFigureSet(figuresList);
                    canvasPanel.PaintFigures();

                }

            }
        });
    }

    private void loadFigures() {

        try{
            FileInputStream fis = new FileInputStream("mydb.fil");
            ObjectInputStream ois = new ObjectInputStream(fis);
            figuresList = (ArrayList<PolygonFigure>)ois.readObject();
            ois.close();
        }
        catch (IOException e){
            System.out.println("***catch ERROR***");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("***catch ERROR***");
            e.printStackTrace();
        }
        System.out.println("Wczytywanie");
        canvasPanel.setFigureSet(figuresList);
        canvasPanel.PaintFigures();


    }

    private void saveFigures() {

        try {

            FileOutputStream fos = new FileOutputStream("mydb.fil");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(figuresList);
            oos.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Zapisywanie");
        canvasPanel.setFigureSet(figuresList);
        canvasPanel.PaintFigures();
    }
}
