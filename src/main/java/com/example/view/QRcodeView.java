package com.example.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.service.QRcodeService;
import com.example.examplefeature.Task;

import java.util.List;

@Route("qrcode")
@PageTitle("QR Code")
@Menu(order = 4, icon = "vaadin:qrcode", title = "QR Code")
public class QRcodeView extends VerticalLayout {

    private final QRcodeService qrCodeService;

    private final Grid<Task> taskGrid = new Grid<>(Task.class, false);
    private final DatePicker datePicker = new DatePicker("Select Date");
    private final Button loadTasksButton = new Button("Load Tasks");
    private final Button editTaskButton = new Button("Edit Task");
    private final Button generateQRButton = new Button("Generate QR Code");
    private final TextArea taskEditor = new TextArea("Edit Task");
    private final Image qrCodeDisplay = new Image();

    private Task selectedTask;

    public QRcodeView(QRcodeService qrCodeService) {
        this.qrCodeService = qrCodeService;

        setSpacing(true);
        setPadding(true);

        // Grid configuration
        taskGrid.addColumn(Task::getTitle).setHeader("Title");
        taskGrid.addColumn(Task::getDueDate).setHeader("Due Date");
        taskGrid.addColumn(Task::getDescription).setHeader("Description");
        taskGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        taskEditor.setWidthFull();
        taskEditor.setVisible(false);
        qrCodeDisplay.setVisible(false);

        loadTasksButton.addClickListener(e -> loadTasks());
        editTaskButton.addClickListener(e -> toggleEditMode());
        generateQRButton.addClickListener(e -> generateQRCode());

        HorizontalLayout controls = new HorizontalLayout(datePicker, loadTasksButton, editTaskButton, generateQRButton);
        add(controls, taskGrid, taskEditor, qrCodeDisplay);
    }

    private void loadTasks() {
        if (datePicker.getValue() == null) {
            Notification.show("Select a date first");
            return;
        }
        List<Task> tasks = qrCodeService.getTasksByDate(datePicker.getValue());
        taskGrid.setItems(tasks);
    }

    private void toggleEditMode() {
        selectedTask = taskGrid.asSingleSelect().getValue();
        if (selectedTask == null) {
            Notification.show("Select a task to edit");
            return;
        }
        taskEditor.setVisible(true);
        taskEditor.setValue(selectedTask.getDescription());
        taskEditor.addValueChangeListener(e -> selectedTask.setDescription(e.getValue()));
    }

    private void generateQRCode() {
        selectedTask = taskGrid.asSingleSelect().getValue();
        if (selectedTask == null) {
            Notification.show("Select a task first");
            return;
        }
        byte[] qrBytes = qrCodeService.generateQRCodeForTask(selectedTask);
        if (qrBytes != null) {
            String qrSrc = "data:image/png;base64," + java.util.Base64.getEncoder().encodeToString(qrBytes);
            qrCodeDisplay.setSrc(qrSrc);
            qrCodeDisplay.setVisible(true);
        }
    }
}
