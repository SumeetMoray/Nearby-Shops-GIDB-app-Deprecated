package nbsidb.nearbyshops.org.ModelItemSpecification;


import java.sql.Timestamp;

/**
 * Created by sumeet on 2/3/17.
 */
public class ItemSpecificationName {


    // Table Name
    public static final String TABLE_NAME = "ITEM_SPECIFICATION_NAME";

    // column names
    public static final String ID = "ID";
    public static final String TITLE = "TITLE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String IMAGE_FILENAME = "IMAGE_FILENAME";
    public static final String GIDB_ID = "GIDB_ID";
    public static final String GIDB_SERVICE_URL = "GIDB_SERVICE_URL";
    public static final String TIMESTAMP_CREATED = "TIMESTAMP_CREATED";


    // create table statement
    public static final String createTableItemSpecNamePostgres = "CREATE TABLE IF NOT EXISTS "
            + ItemSpecificationName.TABLE_NAME + "("
            + " " + ItemSpecificationName.ID + " SERIAL PRIMARY KEY,"
            + " " + ItemSpecificationName.TITLE + " text,"
            + " " + ItemSpecificationName.DESCRIPTION + " text,"
            + " " + ItemSpecificationName.IMAGE_FILENAME + " text,"
            + " " + ItemSpecificationName.GIDB_ID + " int,"
            + " " + ItemSpecificationName.GIDB_SERVICE_URL + " text,"
            + " " + ItemSpecificationName.TIMESTAMP_CREATED + " timestamp with time zone NOT NULL DEFAULT now()"
            + ")";




    // instance variables

    private int id;
    private String title;
    private String description;
    private String imageFilename;
    private int gidbID;
    private String gidbServiceURL;
    private Timestamp timestampCreated;



    // getter and setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    public int getGidbID() {
        return gidbID;
    }

    public void setGidbID(int gidbID) {
        this.gidbID = gidbID;
    }

    public String getGidbServiceURL() {
        return gidbServiceURL;
    }

    public void setGidbServiceURL(String gidbServiceURL) {
        this.gidbServiceURL = gidbServiceURL;
    }

    public Timestamp getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(Timestamp timestampCreated) {
        this.timestampCreated = timestampCreated;
    }
}
