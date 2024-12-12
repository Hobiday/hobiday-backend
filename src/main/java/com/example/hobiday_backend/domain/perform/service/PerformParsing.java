package com.example.hobiday_backend.domain.perform.service;

import com.example.hobiday_backend.domain.perform.entity.FacilityDetail;
import com.example.hobiday_backend.domain.perform.entity.Perform;
import com.example.hobiday_backend.domain.perform.entity.PerformDetail;
import com.example.hobiday_backend.domain.perform.repository.FacilityRepository;
import com.example.hobiday_backend.domain.perform.repository.PerformDetailRepository;
import com.example.hobiday_backend.domain.perform.repository.PerformRepository;
import com.example.hobiday_backend.domain.perform.util.KopisParsing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashSet;

@Slf4j
@RequiredArgsConstructor
@Service
public class PerformParsing extends KopisParsing {
    private final PerformRepository performRepository;
    private final PerformDetailRepository performDetailRepository;
    private final FacilityRepository facilityRepository;

    //시설상세ID 모음
    private static HashSet<String> facilitySet = new HashSet<>();

    public void saveAll() {
//        log.info("파싱 작업 시행");
//        urlBuilder.append("&shcate=" + shcate);
//        urlBuilder.append("&signgucode="+signgucode);
//        urlBuilder.append("&shcate="+shcate);

        // 공연기본, 공연상세 정보 DB 저장
        for (String genreCode : GENRE_CODES_REQUEST.keySet()) {
            savePerformByGenres(genreCode);
//            Thread.sleep(500);
        }

        // 시설상세 정보 DB 저장
        for (String facilityId : facilitySet){
            saveFacility(facilityId);
        }

        log.info("파싱 종료");
    }

    // 단일DB 요청 테스트용
//    public void saveTest() {
//        StringBuilder urlBuilder = new StringBuilder(DETAIL_URL+"PF255029");
//        urlBuilder.append("?service="+SERVICE_KEY);
//
//        NodeList nodeList = getNodeList(urlBuilder);
//        Node node = nodeList.item(0);
//        if (node.getNodeType() == Node.ELEMENT_NODE) {
//            Element element = (Element) node;
//            String facilityId = element.getElementsByTagName("mt10id").item(0).getTextContent();
//            facilitySet.add(facilityId); // 시설상세ID를 집합에 추가
//            log.info("facilityId: " + facilityId);
//            log.info("prfcast: "+element.getElementsByTagName("prfcast").item(0).getTextContent());
//            log.info("prfruntime: "+element.getElementsByTagName("prfruntime").item(0).getTextContent());
//            log.info("prfage: "+element.getElementsByTagName("prfage").item(0).getTextContent());
//            log.info("pcseguidance: "+element.getElementsByTagName("pcseguidance").item(0).getTextContent());
//            log.info("styurl: "+element.getElementsByTagName("styurl").item(0).getTextContent());
//            log.info("dtguidance: "+element.getElementsByTagName("dtguidance").item(0).getTextContent());
//            log.info("relatenm: "+element.getElementsByTagName("relatenm").item(0).getTextContent());
//            log.info("relateurl: "+element.getElementsByTagName("relateurl").item(0).getTextContent());
//        }
//    }

    // 공연기본 저장
    public void savePerformByGenres(String genre) {
        NodeList nodeList = getNodeListByGenre(genre);
        int n = nodeList.getLength();
//        log.info("n:" + n);

        for (int i = 0; i < n; i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String performDetailId = element.getElementsByTagName("mt20id").item(0).getTextContent();
//                log.info("performDetailId: " + performDetailId);

                // 이미 있는 공연 정보이면 패스
                if (performRepository.findByMt20id(performDetailId).isPresent()){
                    continue;
                }

                // 공연 기본정보 저장
                Perform perform = Perform.builder()
//                        .mt20id(element.getElementsByTagName("mt20id").item(0).getTextContent())
                        .mt20id(performDetailId)
                        .prfnm(getTextByElement(element, "prfnm"))
                        .prfpdfrom(getTextByElement(element, "prfpdfrom"))
                        .prfpdto(getTextByElement(element, "prfpdto"))
                        .genrenm(getTextByElement(element, "genrenm"))
                        .fcltynm(getTextByElement(element, "fcltynm"))
                        .area(getTextByElement(element, "area"))
                        .poster(getTextByElement(element, "poster"))
                        .prfstate(getTextByElement(element, "prfstate"))
                        .openrun(getTextByElement(element, "openrun").charAt(0) == 'Y')
                        .build();
                performRepository.save(perform);
                savePerformDetail(performDetailId, perform);
            }
        }
    }

    // 공연상세 저장
    public void savePerformDetail(String performDetailId, Perform perform) {
        StringBuilder urlBuilder = new StringBuilder(DETAIL_URL+performDetailId);
        urlBuilder.append("?service="+SERVICE_KEY);

        Node node = getNodeList(urlBuilder).item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String facilityId = element.getElementsByTagName("mt10id").item(0).getTextContent();
            facilitySet.add(facilityId); // 시설상세ID를 집합에 추가
            performDetailRepository.save(PerformDetail.builder()
                    .perform(perform)
                    .mt20id(performDetailId)
                    .mt10id(facilityId)
                    .prfcast(getTextByElement(element, "prfcast"))
                    .prfruntime(getTextByElement(element, "prfruntime"))
                    .prfage(getTextByElement(element, "prfage"))
                    .pcseguidance(getTextByElement(element, "pcseguidance"))
////                                .sty(element.getElementsByTagName("sty").item(0).getTextContent())
                    .styurl(getTextByElement(element, "styurl"))
                    .dtguidance(getTextByElement(element, "dtguidance"))
                    .relatenm(getTextByElement(element, "relatenm"))
                    .relateurl(getTextByElement(element, "relateurl"))
                    .build());
        }
    }

    // 시설상세 저장
    public void saveFacility(String facilityId) {
        // 이미 있는 시설 정보이면 패스
        if (facilityRepository.findByMt10id(facilityId).isPresent()){
            return;
        }

        StringBuilder urlBuilder = new StringBuilder(FACILITY_URL + facilityId);
        urlBuilder.append("?service="+SERVICE_KEY);

        NodeList nodeList = getNodeList(urlBuilder);
        Node node = nodeList.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            facilityRepository.save(FacilityDetail.builder()
                    .mt10id(facilityId)
                    .fcltynm(getTextByElement(element, "fcltynm"))
                    .telno(getTextByElement(element, "telno"))
                    .adres(getTextByElement(element, "adres"))
                    .la(getTextByElement(element, "la"))
                    .lo(getTextByElement(element, "lo"))
                    .cafe(getTextByElement(element, "cafe").charAt(0) == 'Y')
                    .parkinglot(getTextByElement(element, "parkinglot").charAt(0) == 'Y')
                    .build());
        }
    }

    // (공연기본) 장르 선택하여 여러개의 db 태그 리스트 반환
    public NodeList getNodeListByGenre(String shcate) { // shcate: 장르 코드
//        log.info("장르->노드리스트");
//        String stdate = "20241215"; // 시작 검색기간
//        String eddate = "20240113"; // 종료 검색기간
//        String rows = "5";         // 페이지당 공연 개수
        String cpage = "1";
//        String signgucode = "11";   // 지역 코드
//        String shcate = "AAAA";     // 장르 코드

        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append("?service="+SERVICE_KEY);
        urlBuilder.append("&stdate="+STDATE);
        urlBuilder.append("&eddate="+EDDATE);
        urlBuilder.append("&rows="+ROWS);
        urlBuilder.append("&cpage="+cpage);
//        urlBuilder.append("&signgucode="+signgucode);
        urlBuilder.append("&shcate=" + shcate);
//        log.info("전url: "+urlBuilder.toString());

        return getNodeList(urlBuilder); //파싱할 tag는 <db>
    }

    // (공연상세, 시설상세) 단일 db 태그 리스트 반환
    private static NodeList getNodeList(StringBuilder urlBuilder)  {
//        log.info("후url: "+urlBuilder.toString());
        Document doc = null;
        try {
            doc = DocumentBuilderFactory
                    .newInstance().newDocumentBuilder().parse(String.valueOf(urlBuilder));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        doc.getDocumentElement().normalize();//root tag
        NodeList nodeList = doc.getElementsByTagName("db"); // 파싱할 tag는 <db>
        return nodeList;
    }

    // tag의 첫번째 텍스트 가져오기
    public static String getTextByElement(Element element, String tag){
        return element.getElementsByTagName(tag).item(0).getTextContent();
    }
}