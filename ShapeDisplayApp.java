import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

abstract class Shape {
    abstract void draw(Graphics g, int panelWidth, int panelHeight);
}

class Rectangle extends Shape {
    private int width;
    private int height;

    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    void draw(Graphics g, int panelWidth, int panelHeight) {
        int x = (panelWidth - width * 10) / 2;
        int y = (panelHeight - height * 10) / 2;
        g.drawRect(x, y, width * 10, height * 10);
    }
}

class Triangle extends Shape {
    private int base;
    private int height;

    public Triangle(int base, int height) {
        this.base = base;
        this.height = height;
    }

    @Override
    void draw(Graphics g, int panelWidth, int panelHeight) {
        int[] xPoints = {
            panelWidth / 2,
            panelWidth / 2 + (base * 5),
            panelWidth / 2 - (base * 5)
        };
        int[] yPoints = {
            panelHeight / 2 - (height * 10) / 2,
            panelHeight / 2 + (height * 10) / 2,
            panelHeight / 2 + (height * 10) / 2
        };
        g.drawPolygon(xPoints, yPoints, 3);
    }
}

class Circle extends Shape {
    private int radius;

    public Circle(int radius) {
        this.radius = radius;
    }

    @Override
    void draw(Graphics g, int panelWidth, int panelHeight) {
        int x = (panelWidth - radius * 20) / 2;
        int y = (panelHeight - radius * 20) / 2;
        g.drawOval(x, y, radius * 20, radius * 20);
    }
}

class PolygonShape extends Shape {
    private int sides;
    private int radius;

    public PolygonShape(int sides, int radius) {
        this.sides = sides;
        this.radius = radius;
    }

    @Override
    void draw(Graphics g, int panelWidth, int panelHeight) {
        int[] xPoints = new int[sides];
        int[] yPoints = new int[sides];
        for (int i = 0; i < sides; i++) {
            xPoints[i] = panelWidth / 2 + (int) (radius * 10 * Math.cos(2 * Math.PI * i / sides));
            yPoints[i] = panelHeight / 2 + (int) (radius * 10 * Math.sin(2 * Math.PI * i / sides));
        }
        g.drawPolygon(xPoints, yPoints, sides);
    }
}

public class ShapeDisplayApp extends JFrame {
    private static final int MIN_DIMENSION = 1;
    private static final int MAX_DIMENSION = 20;
    private JTextField dimension1Field;
    private JTextField dimension2Field;
    private JPanel drawingPanel;
    private Shape shape;

    public ShapeDisplayApp() {
        setTitle("Shape Drawer");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel shapeLabel = new JLabel("Choose Shape:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(shapeLabel, gbc);

        String[] shapes = {"Rectangle", "Triangle", "Circle", "Pentagon", "Hexagon", "Heptagon", "Octagon", "Nonagon", "Decagon"};
        JComboBox<String> shapeSelector = new JComboBox<>(shapes);
        shapeSelector.setSelectedIndex(0);
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(shapeSelector, gbc);

        JLabel dimension1Label = new JLabel("Dimension 1 (radius/side/width):");
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(dimension1Label, gbc);

        dimension1Field = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(dimension1Field, gbc);

        JLabel dimension2Label = new JLabel("Dimension 2 (only for Rectangle/Triangle - height):");
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(dimension2Label, gbc);

        dimension2Field = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(dimension2Field, gbc);

        JButton drawButton = new JButton("Draw");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        inputPanel.add(drawButton, gbc);

        add(inputPanel, BorderLayout.NORTH);

        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (shape != null) {
                    shape.draw(g, getWidth(), getHeight());
                }
            }
        };

        add(drawingPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton nextButton = new JButton("Next");
        nextButton.setBackground(Color.GREEN);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(ShapeDisplayApp.this, "Next button clicked!");
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(Color.RED);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(nextButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);

        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String shapeType = (String) shapeSelector.getSelectedItem();
                int dim1 = getDimension(dimension1Field.getText());
                int dim2 = getDimension(dimension2Field.getText());

                switch (shapeType) {
                    case "Rectangle":
                        shape = new Rectangle(dim1, dim2);
                        break;
                    case "Triangle":
                        shape = new Triangle(dim1, dim2);
                        break;
                    case "Circle":
                        shape = new Circle(dim1);
                        break;
                    case "Pentagon":
                    case "Hexagon":
                    case "Heptagon":
                    case "Octagon":
                    case "Nonagon":
                    case "Decagon":
                        int sides = shapeSelector.getSelectedIndex() + 3; // Index 3 corresponds to pentagon (5 sides)
                        shape = new PolygonShape(sides, dim1);
                        break;
                    default:
                        shape = null;
                        break;
                }

                drawingPanel.repaint();
            }
        });
    }

    private int getDimension(String text) {
        try {
            int value = Integer.parseInt(text);
            if (value < MIN_DIMENSION || value > MAX_DIMENSION) {
                JOptionPane.showMessageDialog(this, "Please enter a value between " + MIN_DIMENSION + " and " + MAX_DIMENSION);
                return MIN_DIMENSION;
            }
            return value;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a number.");
            return MIN_DIMENSION;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ShapeDisplayApp frame = new ShapeDisplayApp();
                frame.setVisible(true);
            }
        });
    }
}
