/*
 * Central Repository
 *
 * Copyright 2018 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.centralrepository.optionspanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.openide.util.NbBundle.Messages;
import org.sleuthkit.autopsy.centralrepository.datamodel.CorrelationCase;
import org.sleuthkit.autopsy.centralrepository.datamodel.CorrelationDataSource;
import org.sleuthkit.autopsy.centralrepository.datamodel.EamOrganization;

/**
 * Model for cells to display correlation case information
 */
class ShowCasesTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    /**
     * list of Eam Cases from central repository.
     */
    private final List<TableCaseWrapper> eamCases;

    ShowCasesTableModel() {
        eamCases = new ArrayList<>();
    }

    @Override
    public int getColumnCount() {
        return TableColumns.values().length;
    }

    /**
     * Get the preferred width that has been configured for this column.
     *
     * A value of 0 means that no preferred width has been defined for this
     * column.
     *
     * @param colIdx Column index
     *
     * @return preferred column width >= 0
     */
    int getColumnPreferredWidth(int colIdx) {
        return TableColumns.values()[colIdx].columnWidth();
    }

    @Override
    public int getRowCount() {
        return eamCases.size();
    }

    @Override
    public String getColumnName(int colIdx) {
        return TableColumns.values()[colIdx].columnName();
    }

    @Override
    public Object getValueAt(int rowIdx, int colIdx) {
        if (eamCases.isEmpty()) {
            return Bundle.ShowCasesTableModel_noData();
        }

        return mapValueById(rowIdx, TableColumns.values()[colIdx]);
    }

    Object getRow(int rowIdx) {
        return eamCases.get(rowIdx);
    }

    /**
     * Map a rowIdx and colId to the value in that cell.
     *
     * @param rowIdx Index of row to search
     * @param colId  ID of column to search
     *
     * @return value in the cell
     */
    private Object mapValueById(int rowIdx, TableColumns colId) {
        TableCaseWrapper eamCase = eamCases.get(rowIdx);
        String value = Bundle.ShowCasesTableModel_noData();

        switch (colId) {
            case CASE_NAME:
                value = eamCase.getDisplayName();
                break;
//            case DATA_SOURCE:
//                value = eamCase.getDataSources();
//                break;
            case CREATION_DATE:
                value = eamCase.getCreationDate();
                break;
//            case CASE_NUMBER:
//                value = eamCase.getCaseNumber();
//                break;
//            case EXAMINER_NAME:
//                value = eamCase.getExaminerName();
//                break;
//            case EXAMINER_EMAIL:
//                value = eamCase.getExaminerEmail();
//                break;
//            case EXAMINER_PHONE:
//                value = eamCase.getExaminerPhone();
//                break;
//            case NOTES:
//                value = eamCase.getNotes();
//                break;
            default:
                break;
        }
        return value;
    }

    @Override
    public Class<String> getColumnClass(int colIdx) {
        return String.class;
    }

    /**
     * Add one local central repository case to the table.
     *
     * @param eamCase central repository case to add to the table
     */
    void addEamCase(CorrelationCase eamCase, List<CorrelationDataSource> dataSourceList) {
        eamCases.add(new TableCaseWrapper(eamCase, dataSourceList));
        fireTableDataChanged();
    }

    TableCaseWrapper getEamCase(int listIndex) {
        return eamCases.get(listIndex);
    }

    void clearTable() {
        eamCases.clear();
        fireTableDataChanged();
    }

    @Messages({"ShowCasesTableModel.case=Case Name",
        "ShowCasesTableModel.creationDate=Creation Date",
        "ShowCasesTableModel.noData=No Cases"})
    /**
     * Enum which lists columns of interest from CorrelationCase.
     */
    private enum TableColumns {
        // Ordering here determines displayed column order in Content Viewer.
        // If order is changed, update the CellRenderer to ensure correct row coloring.
        CASE_NAME(Bundle.ShowCasesTableModel_case(), 120),
        CREATION_DATE(Bundle.ShowCasesTableModel_creationDate(), 120);

        private final String columnName;
        private final int columnWidth;

        TableColumns(String columnName, int columnWidth) {
            this.columnName = columnName;
            this.columnWidth = columnWidth;
        }

        String columnName() {
            return columnName;
        }

        int columnWidth() {
            return columnWidth;
        }
    }

    class TableCaseWrapper {

        private final CorrelationCase eamCase;
        private final List<CorrelationDataSource> dataSources;

        TableCaseWrapper(CorrelationCase correlationCase, List<CorrelationDataSource> dataSourceList) {
            eamCase = correlationCase;
            dataSources = dataSourceList;
        }

        String getDisplayName() {
            return eamCase.getDisplayName();
        }

        List<CorrelationDataSource> getDataSources() {
            return Collections.unmodifiableList(dataSources);
        }

        String getCreationDate() {
            return eamCase.getCreationDate();
        }

        String getOrganizationName() {
            EamOrganization org = eamCase.getOrg();
            return org == null ? "" : org.getName();
        }

        String getCaseNumber() {
            return eamCase.getCaseNumber();
        }

        String getExaminerName() {
            return eamCase.getExaminerName();
        }

        String getExaminerEmail() {
            return eamCase.getExaminerEmail();
        }

        String getNotes() {
            return eamCase.getNotes();
        }

        String getExaminerPhone() {
            return eamCase.getExaminerPhone();
        }
    }

}
