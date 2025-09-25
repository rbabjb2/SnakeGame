import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.*;

//Linked list to keep track of snake positions. Movement is remove tail and add to head.

public class Snake implements KeyListener {
    int gridSize = 16;
    int[][] map = new int[gridSize][gridSize];
    JFrame frame = new JFrame("Snake");
    JPanel panel = new JPanel();
    ImageIcon[] icons = new ImageIcon[] { new ImageIcon("src/icons/0.png"), new ImageIcon("src/icons/1.png"),
            new ImageIcon("src/icons/1.png"), new ImageIcon("src/icons/3.png") };
    GridLayout grid = new GridLayout();
    LinkedList<Part> snake = new LinkedList<Part>();
    JLabel[][] labels = new JLabel[gridSize][gridSize];
    boolean isConsumed = true;
    int appleX = 0;
    int appleY = 0;
    boolean canPressKey = true;
    int dir = 1;

    public Snake() {
        snake.add(new Part(8, 7));
        snake.add(new Part(8, 8));
        snake.add(new Part(8, 9));
        snake.removeLast();
        snake.addFirst(new Part(snake.getFirst().x, snake.getFirst().y - 1));
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(this);
        // frame.setResizable(false);
        frame.setVisible(true);

        for (int i = 0; i < icons.length; i++) {
            Image img = icons[i].getImage();
            Image img2 = img.getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
            icons[i] = new ImageIcon(img2);
        }

        grid.setRows(gridSize);
        grid.setColumns(gridSize);

        panel.setLayout(grid);

        /*
         * ImageIcon icon = new ImageIcon("icons/0.png");
         * JLabel label = new JLabel();
         * label.setMinimumSize(new Dimension(300, 200));
         * label.setPreferredSize(new Dimension(300, 200));
         * label.setMaximumSize(new Dimension(300, 200));
         * label.setText("fghxsrs");
         * //label.setIcon(icon);
         */

        fillArray();
        updateScreen(snake.getFirst(), 0);
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                panel.add(labels[j][i]);
            }
        }
        panel.setBackground(Color.BLUE);
        frame.add(panel);
        panel.invalidate();
        panel.revalidate();
        panel.repaint();
        while (true) {
            try {

                if (snake.getFirst().x == appleX && snake.getFirst().y == appleY) {
                    System.out.println("Ate the apple");
                    isConsumed = true;
                } else {
                    snake.removeLast();
                }

                if (dir == 0) {
                    snake.addFirst(new Part(snake.getFirst().x, snake.getFirst().y - 1));
                } else if (dir == 1) {
                    snake.addFirst(new Part(snake.getFirst().x + 1, snake.getFirst().y));
                } else if (dir == 2) {
                    snake.addFirst(new Part(snake.getFirst().x, snake.getFirst().y + 1));
                } else if (dir == 3) {
                    snake.addFirst(new Part(snake.getFirst().x - 1, snake.getFirst().y));
                } else {
                    System.out.println("dir is not 0,1,2 or 3");
                }

                for (Part i : snake) {
                    if (snake.getFirst() != i && snake.getFirst().x == i.x && snake.getFirst().y == i.y) {
                        System.exit(0);
                    }
                }
                if (snake.getFirst().x < 0 || snake.getFirst().y < 0 || snake.getFirst().x >= 16 || snake.getFirst().y >= 16) {
                    System.exit(0);
                }

                updateScreen(snake.getFirst(), 0);

                Thread.sleep(500);
            } catch (Exception e) {
            }
            canPressKey = true;
        }
    }

    public static void main(String[] args) {
        new Snake();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (canPressKey) {
            canPressKey = false;
            switch (keyCode) {
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
                    break;
                case KeyEvent.VK_W:
                    if (dir != 2) {
                        dir = 0;
                    }
                    break;
                case KeyEvent.VK_D:
                    if (dir != 3) {
                        dir = 1;
                    }
                    break;
                case KeyEvent.VK_S:
                    if (dir != 0) {
                        dir = 2;
                    }
                    break;
                case KeyEvent.VK_A:
                    if (dir != 1) {
                        dir = 3;
                    }
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void fillArray() {

        isConsumed = true;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                map[j][i] = 0;
                labels[j][i] = new JLabel();
                labels[j][i].setIcon(icons[map[j][i]]);
                panel.add(labels[j][i]);
            }
        }
        map[1][1] = 3;
    }

    public void updateScreen(Part part, int index) {
        index++;
        if (part == snake.getFirst()) {
            if (isConsumed == true) {
                Random random = new Random();
                appleX = random.nextInt(gridSize);
                appleY = random.nextInt(gridSize);
                isConsumed = false;
            }
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {

                    if (j == appleX && i == appleY) {
                        map[j][i] = 3;
                    } else {
                        map[j][i] = 0;
                    }
                }
            }
        }
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (part.x == j && part.y == i) {
                    map[j][i] = 1;
                }
                labels[i][j].setIcon(icons[map[i][j]]);
            }
        }
        if (part != snake.getLast()) {
            updateScreen(snake.get(index), index);
        } else {
            frame.invalidate();
            frame.validate();
            frame.repaint();
        }
    }
}
