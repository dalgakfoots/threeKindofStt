package onthelive.threeKindofStt.service;

public interface SpeechToTextService {
    String speechToText(String filePath) throws Exception; // filePath : 다운로드 받은 파일이 저장된 위치 (오브젝트 스토리지 내 경로가 아님)
}
