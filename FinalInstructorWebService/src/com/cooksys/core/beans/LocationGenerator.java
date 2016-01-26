package com.cooksys.core.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.core.models.Location;

@Component
@Scope("singleton")
public class LocationGenerator {
	private static final String TENNESSEE = "Tennessee";
	private static final String NASHVILLE = "Nashville";
	private static final String MEMPHIS = "Memphis";
	private static final String KNOXVILLE = "Knoxville";
	private static final String CHATTANOOGA = "Chattanooga";

	private static final String ARKANSAS = "Arkansas";
	private static final String LITTLEROCK = "Little Rock";
	private static final String FORTSMITH = "Fort Smith";
	private static final String JONESBORO = "Jonesboro";
	private static final String TEXARKANA = "Texarkana";

	private static final String LOUISIANA = "Louisiana";
	private static final String SHREVEPORT = "Shreveport";
	private static final String LAKECHARLES = "Lake Charles";
	private static final String BATONROUGE = "Baton Rouge";
	private static final String ALEXANDRIA = "Alexandria";

	private static final String MISSISSIPPI = "Mississippi";
	private static final String COLUMBUS = "Columbus";
	private static final String JACKSON = "Jackson";
	private static final String TUPELO = "Tupelo";
	private static final String HATTIESBURG = "Hattiesburg";

	private static final String ALABAMA = "Alabama";
	private static final String BIRMINGHAM = "Birmingham";
	private static final String JASPER = "Jasper";
	private static final String MONTGOMERY = "Montgomery";
	private static final String DOTHAN = "Dothan";

	private static final String GEORGIA = "Georgia";
	private static final String ATLANTA = "Atlanta";
	private static final String MACON = "Macon";
	private static final String ATHENS = "Athens";
	private static final String ALBANY = "Albany";

	private List<Location> majorLocations;
	private List<Location> minorLocations;

	@PostConstruct
	public void init() {

		majorLocations = new ArrayList<Location>(6);
		minorLocations = new ArrayList<Location>(18);

		majorLocations.add(new Location(TENNESSEE, NASHVILLE));
		majorLocations.add(new Location(ARKANSAS, LITTLEROCK));
		majorLocations.add(new Location(LOUISIANA, SHREVEPORT));
		majorLocations.add(new Location(MISSISSIPPI, COLUMBUS));
		majorLocations.add(new Location(ALABAMA, BIRMINGHAM));
		majorLocations.add(new Location(GEORGIA, ATLANTA));

		minorLocations.add(new Location(TENNESSEE, MEMPHIS));
		minorLocations.add(new Location(TENNESSEE, KNOXVILLE));
		minorLocations.add(new Location(TENNESSEE, CHATTANOOGA));

		minorLocations.add(new Location(ARKANSAS, FORTSMITH));
		minorLocations.add(new Location(ARKANSAS, JONESBORO));
		minorLocations.add(new Location(ARKANSAS, TEXARKANA));

		minorLocations.add(new Location(LOUISIANA, LAKECHARLES));
		minorLocations.add(new Location(LOUISIANA, BATONROUGE));
		minorLocations.add(new Location(LOUISIANA, ALEXANDRIA));

		minorLocations.add(new Location(MISSISSIPPI, JACKSON));
		minorLocations.add(new Location(MISSISSIPPI, TUPELO));
		minorLocations.add(new Location(MISSISSIPPI, HATTIESBURG));

		minorLocations.add(new Location(ALABAMA, JASPER));
		minorLocations.add(new Location(ALABAMA, MONTGOMERY));
		minorLocations.add(new Location(ALABAMA, DOTHAN));

		minorLocations.add(new Location(GEORGIA, MACON));
		minorLocations.add(new Location(GEORGIA, ATHENS));
		minorLocations.add(new Location(GEORGIA, ALBANY));

	}

	public List<Location> getMajorLocations() {
		return majorLocations;
	}

	public void setMajorLocations(List<Location> majorLocations) {
		this.majorLocations = majorLocations;
	}

	public List<Location> getMinorLocations() {
		return minorLocations;
	}

	public void setMinorLocations(List<Location> minorLocations) {
		this.minorLocations = minorLocations;
	}
	
}
