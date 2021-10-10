package is.symphony.test.javacro.stats.emitter.service.model;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.UUID;

@Table(CompanyDB.TABLE_NAME)
public class CompanyDB {
    public static final String TABLE_NAME = "company";
    public static final String COUNTRY = "country";
    public static final String CITY = "city";
    public static final String ID = "id";

    @PrimaryKeyColumn(
            name = ID,
            ordinal = 1,
            type = PrimaryKeyType.CLUSTERED,
            ordering = Ordering.DESCENDING)
    private UUID id;

    @PrimaryKeyColumn(
            name = COUNTRY,
            ordinal = 0,
            type = PrimaryKeyType.PARTITIONED)
    private String country;

    @PrimaryKeyColumn(
            name = CITY,
            ordinal = 2,
            type = PrimaryKeyType.PARTITIONED)
    private String city;
    private String name;
    private String address;
    private String email;
    private String sector;
    private String streetNumber;
    private String postalCode;
    private ByteBuffer map;

    public CompanyDB() {
    }


    public UUID getId() {
        return id;
    }

    public CompanyDB setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public CompanyDB setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCity() {
        return city;
    }

    public CompanyDB setCity(String city) {
        this.city = city;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public CompanyDB setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CompanyDB setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getSector() {
        return sector;
    }

    public CompanyDB setSector(String sector) {
        this.sector = sector;
        return this;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public CompanyDB setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public CompanyDB setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public ByteBuffer getMap() {
        return map;
    }

    public CompanyDB setMap(ByteBuffer map) {
        this.map = map;
        return this;
    }

    public String getName() {
        return name;
    }

    public CompanyDB setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyDB companyDB = (CompanyDB) o;
        return Objects.equals(id, companyDB.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CompanyDB{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", sector='" + sector + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}

