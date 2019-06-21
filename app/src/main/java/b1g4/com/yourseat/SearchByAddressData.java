package b1g4.com.yourseat;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public final class SearchByAddressData implements Serializable {
    @SerializedName("meta") Meta meta;
    @SerializedName("documents") ArrayList<Document> documents;

    public final class Meta implements Serializable {
        @SerializedName("total_count") int total_count;
        @SerializedName("pageable_count") int pageable_count;
        @SerializedName("is_end") boolean is_end;
        public Meta(int total_count, int pageable_count, boolean is_end) {
            this.total_count = total_count;
            this.pageable_count = pageable_count;
            this.is_end = is_end;
        }
    }
    public final class Document implements Serializable {
        @SerializedName("address_name") String address_name;
        @SerializedName("address_type") String address_type;
        @SerializedName("x") String x;
        @SerializedName("y") String y;
        @SerializedName("address") Address address;
        @SerializedName("road_address") RoadAddress road_address;

        public Document(String address_name, String address_type, String x, String y, Address address, RoadAddress road_address) {
            this.address_name = address_name;
            this.address_type = address_type;
            this.x = x;
            this.y = y;
            this.address = address;
            this.road_address = road_address;
        }
    }

    public final class Address implements Serializable {
        @SerializedName("address_name") String address_name;
        @SerializedName("region_1depth_name") String region_1depth_name;
        @SerializedName("region_2depth_name") String region_2depth_name;
        @SerializedName("region_3depth_name") String region_3depth_name	;
        @SerializedName("region_3depth_h_name") String region_3depth_h_name;
        @SerializedName("h_code") String h_code;
        @SerializedName("b_code") String b_code;
        @SerializedName("mountain_yn") String mountain_yn;
        @SerializedName("main_address_no") String main_address_no;
        @SerializedName("sub_address_no") String sub_address_no;
        @SerializedName("zip_code") String zip_code;
        @SerializedName("x") String x;
        @SerializedName("y") String y;


        public Address(String address_name, String region_1depth_name, String region_2depth_name, String region_3depth_name, String region_3depth_h_name, String h_code, String b_code, String mountain_yn, String main_address_no, String sub_address_no, String zip_code, String x, String y) {
            this.address_name = address_name;
            this.region_1depth_name = region_1depth_name;
            this.region_2depth_name = region_2depth_name;
            this.region_3depth_name = region_3depth_name;
            this.region_3depth_h_name = region_3depth_h_name;
            this.h_code = h_code;
            this.b_code = b_code;
            this.mountain_yn = mountain_yn;
            this.main_address_no = main_address_no;
            this.sub_address_no = sub_address_no;
            this.zip_code = zip_code;
            this.x = x;
            this.y = y;
        }
    }

    public final class RoadAddress implements Serializable{
        @SerializedName("address_name") String address_name;
        @SerializedName("region_1depth_name") String region_1depth_name;
        @SerializedName("region_2depth_name") String region_2depth_name;
        @SerializedName("region_3depth_name") String region_3depth_name;
        @SerializedName("road_name") String road_name;
        @SerializedName("underground_yn") String underground_yn;
        @SerializedName("main_address_no") String main_address_no;
        @SerializedName("sub_address_no") String sub_address_no;
        @SerializedName("building_name") String building_name;
        @SerializedName("zone_no") String zone_no;
        @SerializedName("x") String x;
        @SerializedName("y") String y;


        public RoadAddress(String address_name, String region_1depth_name, String region_2depth_name, String region_3depth_name, String road_name, String underground_yn, String main_address_no, String sub_address_no, String building_name, String zone_no, String x, String y) {
            this.address_name = address_name;
            this.region_1depth_name = region_1depth_name;
            this.region_2depth_name = region_2depth_name;
            this.region_3depth_name = region_3depth_name;
            this.road_name = road_name;
            this.underground_yn = underground_yn;
            this.main_address_no = main_address_no;
            this.sub_address_no = sub_address_no;
            this.building_name = building_name;
            this.zone_no = zone_no;
            this.x = x;
            this.y = y;
        }
    }
}