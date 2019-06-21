package b1g4.com.yourseat;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class SearchByKeywordData implements Serializable {
    @SerializedName("meta") Meta meta;
    @SerializedName("documents") ArrayList<Document> documents;

    public final class Meta implements Serializable {
        @SerializedName("total_count") int total_count;              // 검색어에 검색된 문서수
        @SerializedName("pageable_count") int pageable_count;       // total_count 중에 노출가능 문서수. 최대 45.
        @SerializedName("is_end") boolean is_end;                     // 현재 페이지가 마지막 페이지인지 여부. 값이 false이면 page를 증가시켜 다음 페이지를 요청할 수 있음.
        @SerializedName("same_name") SameName same_name;              // 질의어의 지역/키워드 분석 정보
        public Meta(int total_count, int pageable_count, boolean is_end, SameName same_name) {
            this.total_count = total_count;
            this.pageable_count = pageable_count;
            this.is_end = is_end;
            this.same_name = same_name;
        }
    }

    public final class Document implements Serializable {
        @SerializedName("id") String id;                                            // 장소 ID
        @SerializedName("place_name") String place_name;                         // 장소명, 업체명
        @SerializedName("category_name") String category_name;                  // 카테고리 이름
        @SerializedName("category_group_code") String category_group_code;    // 중요 카테고리만 그룹핑한 카테고리 그룹 코드. Request에 category_group_code 테이블 참고
        @SerializedName("category_group_name") String category_group_name;    // 중요 카테고리만 그룹핑한 카테고리 그룹명. Request에 category_group_code 테이블 참고
        @SerializedName("phone") String phone;                                     // 전화번호                     // 전화번호
        @SerializedName("address_name") String address_name;                     // 전체 지번 주소
        @SerializedName("road_address_name") String road_address_name;         // 전체 도로명 주소
        @SerializedName("x") String x;                                              // X 좌표값 혹은 longitude
        @SerializedName("y") String y;                                              // Y 좌표값 혹은 latitude
        @SerializedName("place_url") String place_url;                           // 장소 상세페이지 URL
        @SerializedName("diatance") String distance;                             // 중심좌표까지의 거리(x,y 파라미터를 준 경우에만 존재). 단위 meter

        public Document(String id, String place_name, String category_name, String category_group_code, String category_group_name, String phone, String address_name, String road_address_name, String x, String y, String place_url, String distance) {
            this.id = id;
            this.place_name = place_name;
            this.category_name = category_name;
            this.category_group_code = category_group_code;
            this.category_group_name = category_group_name;
            this.phone = phone;
            this.address_name = address_name;
            this.road_address_name = road_address_name;
            this.x = x;
            this.y = y;
            this.place_url = place_url;
            this.distance = distance;
        }
    }


    public final class SameName implements Serializable {
        @SerializedName("region") List<String> regions;                     // 질의어에서 인식된 지역의 리스트. '중앙로 맛집' 에서 중앙로에 해당하는 지역 리스트
        @SerializedName("keyword") String keyword;                         // 질의어에서 지역 정보를 제외한 키워드. '중앙로 맛집' 에서 '맛집'
        @SerializedName("selected_region") String selected_region;      // 인식된 지역 리스트 중, 현재 검색에 사용된 지역 정보.

        public SameName(List<String> regions, String keyword, String selected_region) {
            this.regions = regions;
            this.keyword = keyword;
            this.selected_region = selected_region;
        }
    }
}