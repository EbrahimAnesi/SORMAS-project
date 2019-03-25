/*******************************************************************************
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
 *******************************************************************************/
package de.symeda.sormas.api.event;

import java.io.Serializable;
import java.util.Date;

import de.symeda.sormas.api.BaseCriteria;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseCriteria;
import de.symeda.sormas.api.caze.NewCaseDateType;
import de.symeda.sormas.api.region.DistrictReferenceDto;
import de.symeda.sormas.api.region.RegionReferenceDto;
import de.symeda.sormas.api.user.UserRole;

public class EventCriteria extends BaseCriteria implements Serializable {

	private static final long serialVersionUID = 2194071020732246594L;
	
	private EventStatus eventStatus;
	private EventType eventType;
	private Disease disease;
	private UserRole reportingUserRole;
	private Boolean archived;
	private RegionReferenceDto region;
	private DistrictReferenceDto district;
	private Date newEventDateFrom;
	private Date newEventDateTo;
	
	public EventStatus getEventStatus() {
		return eventStatus;
	}
	public EventCriteria eventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
		return this;
	}
	public EventType getEventType() {
		return eventType;
	}
	public EventCriteria eventType(EventType eventType) {
		this.eventType = eventType;
		return this;
	}
	public Disease getDisease() {
		return disease;
	}
	public EventCriteria disease(Disease disease) {
		this.disease = disease;
		return this;
	}
	public UserRole getReportingUserRole() {
		return reportingUserRole;
	}
	public EventCriteria reportingUserRole(UserRole reportingUserRole) {
		this.reportingUserRole = reportingUserRole;
		return this;
	}
	public Boolean getArchived() {
		return archived;
	}
	public EventCriteria archived(Boolean archived) {
		this.archived = archived;
		return this;
	}
	public EventCriteria region(RegionReferenceDto region) {
		this.region = region;
		return this;
	}
	public RegionReferenceDto getRegion() {
		return this.region;
	}
	public EventCriteria district(DistrictReferenceDto district) {
		this.district = district;
		return this;
	}
	public DistrictReferenceDto getDistrict() {
		return this.district;
	}
	/**
	 * @param newEventDateTo will automatically be set to the end of the day
	 */
	public EventCriteria newEventDateBetween(Date newEventDateFrom, Date newEventDateTo) {
		this.newEventDateFrom = newEventDateFrom;
		this.newEventDateTo = newEventDateTo;
		return this;
	}
	public Date getNewEventDateFrom() {
		return newEventDateFrom;
	}
	public Date getNewEventDateTo() {
		return newEventDateTo;
	}
}
