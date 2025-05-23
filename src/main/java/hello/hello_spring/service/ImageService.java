package hello.hello_spring.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {
    //이미지 저장 경로
    private final Path imageDir = Paths.get("images").toAbsolutePath();

    //이미지 저장하고 접근할 수 있 url 반환하는 메소드
    public String saveImageGetUrl(MultipartFile image) throws IOException {
        String originalName = image.getOriginalFilename(); //이미지 원본 이름
        String extension = getExtension(originalName); // png, jpg

        //랜덤 문자열로 이름 저장하기
        String savedFileName = UUID.randomUUID().toString()+"."+extension;

        // .../imageDir/savedFilename 으로 경로를 설정하는 메소드 (.../images/e2dsf2dgn.png) 이런 식의 경로가 됩니다.
        Path savedPath = imageDir.resolve(savedFileName);
        image.transferTo(savedPath.toFile()); //실제 이미지 파일 저장

        return "/image/" + savedFileName; //저장한 이미지 조회할 수 있는 url WebConfig에서 매핑해줌
    }

    //getExtension 메소드
    public String getExtension(String filename) throws IOException{
        if (filename == null || filename.isBlank() || !filename.contains(".")){
            throw new IOException("확장자 없는 이미지 파일");
        }
        return filename.substring(filename.lastIndexOf(".")+1);
    }
}
