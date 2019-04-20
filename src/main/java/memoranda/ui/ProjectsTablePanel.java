package main.java.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.interfaces.ACurrentProject;
import main.java.memoranda.interfaces.AProject;
import main.java.memoranda.interfaces.AProjectManager;
import main.java.memoranda.util.Local;

/*$Id: ProjectsTablePanel.java,v 1.6 2004/04/05 10:05:44 alexeya Exp $*/
public class ProjectsTablePanel extends JPanel {
    BorderLayout borderLayout1 = new BorderLayout();
    JScrollPane scrollPane = new JScrollPane();
    public JTable projectsTable = new JTable() {
        public TableCellRenderer getCellRenderer(int row, int column) {
            if (((String) getModel().getValueAt(row, PROJECT_ID)).equals(ACurrentProject.getProject().getID())) {
                return new javax.swing.table.DefaultTableCellRenderer() {
                    public Component getTableCellRendererComponent(
                        JTable table,
                        Object value,
                        boolean isSelected,
                        boolean hasFocus,
                        int row,
                        int column) {
                        Component comp =
                            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        comp.setFont(new java.awt.Font("Dialog", 1, 11));
                        if (((row % 2) > 0) && (!isSelected))
                            comp.setBackground(new Color(230, 240, 255));
                        return comp;
                    }
                };
            }
            if ((row % 2) > 0) {
                return new javax.swing.table.DefaultTableCellRenderer() {
                    public Component getTableCellRendererComponent(
                        JTable table,
                        Object value,
                        boolean isSelected,
                        boolean hasFocus,
                        int row,
                        int column) {
                        Component comp =
                            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        if (isSelected)
                            return comp;
                        comp.setBackground(new Color(230, 240, 255));
                        return comp;
                    }
                };
            }
            return super.getCellRenderer(row, column);
        }
    };

    boolean activeOnly = false;

    public void updateUI() {
		if(projectsTable!=null) projectsTable.updateUI();
		super.updateUI();
    }

    public ProjectsTablePanel() {
        try {
            jbInit();
        }
        catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }
    void jbInit() throws Exception {
        projectsTable.getTableHeader().setFont(new java.awt.Font("Dialog", 1, 10));
        projectsTable.setFont(new java.awt.Font("Dialog", 0, 11));
        projectsTable.setMinimumSize(new Dimension(200, 100));
        projectsTable.setMaximumSize(new Dimension(32767, 32767));
        //projectsTable.setPreferredSize(new Dimension(400, 100));
        projectsTable.setGridColor(new Color(230, 230, 230));
        projectsTable.setShowHorizontalLines(false);
        /*projectsTable.setSelectionBackground(Color.white);
        projectsTable.setSelectionForeground(Color.blue);*/
        scrollPane.getViewport().setBackground(Color.white);
        scrollPane.getViewport().add(projectsTable, null);
        this.setLayout(borderLayout1);
        this.add(scrollPane, BorderLayout.CENTER);
        initProjectsTable();
    }

    void initProjectsTable() {
        projectsTable.setModel(new PrjTableModel());
        for (int i = 0; i < 4; i++) {
            TableColumn column = projectsTable.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(32767);
            }
            else {
                column.setMinWidth(80);
                column.setPreferredWidth(80);                
            }
        }
    }

    public void setShowActiveOnly(boolean shao) {
        activeOnly = shao;
        projectsTable.updateUI();
        //projectsTable.setModel(new PrjTableModel());
    }

    public String getSelectedProjectID() {
        return (String) projectsTable.getModel().getValueAt(projectsTable.getSelectedRow(), PROJECT_ID);
    }

    public AProject getSelectedProject() {
        return (AProject) projectsTable.getModel().getValueAt(projectsTable.getSelectedRow(), PROJECT);
    }

    static final int PROJECT = 101;
    static final int PROJECT_ID = 100;

    class PrjTableModel extends AbstractTableModel {

        String[] columnNames =
            {
                Local.getString("Project title"),
                Local.getString("Start date"),
                Local.getString("End date"),
                //Local.getString("Execution"),
                Local.getString("Status")};

        PrjTableModel() {
            super();
        }

        public int getColumnCount() {
            return 4;
        }

        public Object getValueAt(int row, int col) {
			if(row==-1) return "";
			AProject pr;
			if (activeOnly)
				pr = (AProject) AProjectManager.getActiveProjects().get(row);
			else
				pr = (AProject) AProjectManager.getAllProjects().get(row);
            switch (col) {
                case 0 :
                    return pr.getTitle();
                case 1 :
                    return pr.getStartDate().getShortDateString();
                case 2 :
                    CalendarDate d = pr.getEndDate();
                    if (d == null)
                        return "-";
                    else
                        return d.getShortDateString();
                //case 3 :   return pr.getProgress() + "%";
                case 3 :
                    return getStatusString(pr.getStatus());
                case 100 :
                    return pr.getID();
                case 101 :
                    return pr;
            }
            return "";
        }

        public int getRowCount() {
            if (activeOnly)
                return AProjectManager.getActiveProjectsNumber();
            return AProjectManager.getAllProjectsNumber();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }
    }

    String getStatusString(int status) {
        switch (status) {
            case AProject.ACTIVE :
                return Local.getString("Active");
            case AProject.COMPLETED :
                return Local.getString("Completed");
            case AProject.FAILED :
                return Local.getString("Failed");
            case AProject.FROZEN :
                return Local.getString("Frozen");
            case AProject.SCHEDULED :
                return Local.getString("Scheduled");
        }
        return "";
    }
}