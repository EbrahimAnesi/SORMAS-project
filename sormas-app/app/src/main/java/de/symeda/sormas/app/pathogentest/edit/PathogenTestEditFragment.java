/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.app.pathogentest.edit;

import android.util.Log;
import android.view.View;

import java.util.List;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.facility.FacilityDto;
import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.PathogenTestType;
import de.symeda.sormas.api.sample.SamplePurpose;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.caze.Case;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.facility.Facility;
import de.symeda.sormas.app.backend.sample.PathogenTest;
import de.symeda.sormas.app.backend.sample.Sample;
import de.symeda.sormas.app.caze.edit.CaseNewActivity;
import de.symeda.sormas.app.caze.read.CaseReadActivity;
import de.symeda.sormas.app.component.Item;
import de.symeda.sormas.app.component.controls.ControlPropertyField;
import de.symeda.sormas.app.component.controls.ValueChangeListener;
import de.symeda.sormas.app.databinding.FragmentPathogenTestEditLayoutBinding;
import de.symeda.sormas.app.util.DataUtils;
import de.symeda.sormas.app.util.DiseaseConfigurationCache;

import static android.view.View.GONE;

public class PathogenTestEditFragment extends BaseEditFragment<FragmentPathogenTestEditLayoutBinding, PathogenTest, PathogenTest> {

    private PathogenTest record;
    private Sample sample;

    // Enum lists

    private List<Facility> labList;
    private List<Item> testTypeList;
    private List<Item> diseaseList;
    private List<Item> testResultList;

    // Instance methods

    public static PathogenTestEditFragment newInstance(PathogenTest activityRootData) {
        return newInstance(PathogenTestEditFragment.class, null, activityRootData);
    }

    // Overrides

    @Override
    protected String getSubHeadingTitle() {
        return getResources().getString(R.string.heading_pathogen_test_edit);
    }

    @Override
    public PathogenTest getPrimaryData() {
        return record;
    }

    @Override
    protected void prepareFragmentData() {
        record = getActivityRootData();
        sample = record.getSample();
        testTypeList = DataUtils.getEnumItems(PathogenTestType.class, true);
        diseaseList = DataUtils.toItems(DiseaseConfigurationCache.getInstance().getAllDiseases(true, null, true));
        testResultList = DataUtils.getEnumItems(PathogenTestResultType.class, true);
        labList = DatabaseHelper.getFacilityDao().getLaboratories(true);
    }

    @Override
    public void onLayoutBinding(FragmentPathogenTestEditLayoutBinding contentBinding) {
        contentBinding.setData(record);
    }

    @Override
    public void onAfterLayoutBinding(FragmentPathogenTestEditLayoutBinding contentBinding) {
        // Initialize ControlSpinnerFields
        contentBinding.pathogenTestTestType.initializeSpinner(testTypeList);
        contentBinding.pathogenTestTestedDisease.initializeSpinner(diseaseList);
        contentBinding.pathogenTestTestResult.initializeSpinner(testResultList);
        contentBinding.pathogenTestLab.initializeSpinner(DataUtils.toItems(labList), new ValueChangeListener() {
            @Override
            public void onChange(ControlPropertyField field) {
                Facility laboratory = (Facility) field.getValue();
                if (laboratory != null && laboratory.getUuid().equals(FacilityDto.OTHER_LABORATORY_UUID)) {
                    contentBinding.pathogenTestLabDetails.setVisibility(View.VISIBLE);
                } else {
                    contentBinding.pathogenTestLabDetails.hideField(true);
                }
            }
        });

//        // Initialize ControlDateFields
        contentBinding.pathogenTestTestDateTime.initializeDateTimeField(getFragmentManager());

        if (sample.getSamplePurpose() == SamplePurpose.INTERNAL) {
            contentBinding.pathogenTestLab.setRequired(false);
        }
    }

    @Override
    public int getEditLayout() {
        return R.layout.fragment_pathogen_test_edit_layout;
    }
}
