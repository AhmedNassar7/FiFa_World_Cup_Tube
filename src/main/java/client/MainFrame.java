package client;

import app.Video;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import utils.Stack;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

import java.awt.event.*;
import java.io.File;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JButton selectButton;
    private JTextField videoPathTextField;
    private JTextField videoTitleTextField;
    private JTextField videoTagsTextField;
    private JTextArea videoDescTextArea;
    private JButton uploadButton;
    private JButton videoCoverSelectButton;
    private JTextField videoCoverPathTextField;
    private JTextField searchTextField;
    private JButton searchButton;
    private JScrollPane videosScrollPane;
    private JList<Video> videosList;
    private JPanel videoPlayPanel;
    private JButton backHistoryButton;
    private JButton forwardHistoryButton;
    private JButton playPauseButton;
    private final DefaultListModel<Video> videosListModel;
    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private final Stack<Video> backwardStack;
    private final Stack<Video> forwardStack;

    public class VideosListRenderer extends DefaultListCellRenderer {

        Font font = new Font("helvitica", Font.BOLD, 18);

        @Override
        public Component getListCellRendererComponent(
                JList list, Object video, int index,
                boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, "<html>" + ((Video) video).title + "<br/>" + "<br/>" + ((Video) video).description + "</html>",
                    index, isSelected, cellHasFocus);
            Image image = new ImageIcon(((Video) video).getCover().getAbsolutePath()).getImage();
            label.setIcon(new ImageIcon(image.getScaledInstance(image.getWidth(null) / 3, image.getHeight(null) / 3, Image.SCALE_SMOOTH)));
            label.setHorizontalTextPosition(JLabel.RIGHT);
            label.setFont(font);
            label.setBorder(new EmptyBorder(20, 20, 20, 20));
            return label;
        }
    }

    public MainFrame() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("World Cup Tube");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setContentPane(panel1);
        videosListModel = new DefaultListModel<>();
        videosList.setCellRenderer(new VideosListRenderer());
        videosList.setBorder(new EmptyBorder(20, 20, 20, 20));

        videosList.setModel(videosListModel);
        listVideos();
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        videoPlayPanel.add(mediaPlayerComponent, BorderLayout.CENTER);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 720));
        backHistoryButton.setEnabled(false);
        forwardHistoryButton.setEnabled(false);

        pack();
        backwardStack = new Stack<>();
        forwardStack = new Stack<>();
        setVisible(true);

        selectButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Video Files", "mp4", "avi", "mkv");
            fileChooser.setFileFilter(filter);
            fileChooser.setDialogTitle("Select Video");
            int res = fileChooser.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                videoPathTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                String fileName = fileChooser.getSelectedFile().getName();
                int pos = fileName.lastIndexOf(".");
                if (pos > 0 && pos < (fileName.length() - 1)) {
                    fileName = fileName.substring(0, pos);
                }
                videoTitleTextField.setText(fileName);
            }
        });
        videoCoverSelectButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg");
            fileChooser.setFileFilter(filter);
            fileChooser.setDialogTitle("Select Image");
            int res = fileChooser.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                videoCoverPathTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        uploadButton.addActionListener(actionEvent -> {
            String title = videoTitleTextField.getText();
            String desc = videoDescTextArea.getText();
            String tags = videoTagsTextField.getText();
            String path = videoPathTextField.getText();
            String coverPath = videoCoverPathTextField.getText();
            if (title.isEmpty() || desc.isEmpty() || tags.isEmpty() || path.isEmpty() || coverPath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }
            String[] tagsArray = tags.split(",");
            for (int i = 0; i < tagsArray.length; i++) {
                tagsArray[i] = tagsArray[i].trim();
            }
            Video video = Video.add(new File(path), new File(coverPath), title, desc, tagsArray);
            videosListModel.addElement(video);
            JOptionPane.showMessageDialog(this, "Video uploaded successfully");
        });
        searchButton.addActionListener(actionEvent -> {
            String query = searchTextField.getText();
            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search query");
                return;
            }
            videosListModel.clear();
            for (Video video : Video.search(query)) {
                videosListModel.addElement(video);
            }
        });
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int index = videosList.locationToIndex(e.getPoint());
                Video video = videosListModel.get(index);
                mediaPlayerComponent.mediaPlayer().media().start(video.getFile().getAbsolutePath());
                tabbedPane1.setSelectedIndex(2);
                backwardStack.push(video);
                if (backwardStack.size() > 1)
                    backHistoryButton.setEnabled(true);

            }
        };
        videosList.addMouseListener(mouseListener);

        playPauseButton.addActionListener(actionEvent -> {
            if (mediaPlayerComponent.mediaPlayer().status().isPlaying()) {
                mediaPlayerComponent.mediaPlayer().controls().pause();
            } else {
                mediaPlayerComponent.mediaPlayer().controls().play();
            }
        });
        backHistoryButton.addActionListener(actionEvent -> {
            if (backwardStack.size() > 1) {
                Video poppedVideo = backwardStack.pop();
                if (backwardStack.size() == 1)
                    backHistoryButton.setEnabled(false);
                forwardStack.push(poppedVideo);
                forwardHistoryButton.setEnabled(true);
                Video video = backwardStack.peek();
                mediaPlayerComponent.mediaPlayer().media().start(video.getFile().getAbsolutePath());
            }
        });
        forwardHistoryButton.addActionListener(actionEvent -> {
            if (forwardStack.size() >= 1) {
                Video video = forwardStack.pop();
                if (forwardStack.isEmpty())
                    forwardHistoryButton.setEnabled(false);
                backwardStack.push(video);
                backHistoryButton.setEnabled(true);
                mediaPlayerComponent.mediaPlayer().media().start(video.getFile().getAbsolutePath());
            }
        });


    }

    private void listVideos() {
        for (Video video : Video.getVideos()) {
            videosListModel.addElement(video);
        }
    }
}
