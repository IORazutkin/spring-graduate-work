package com.iorazutkin.graduatework.service;

import com.iorazutkin.graduatework.exception.BadRequestException;
import com.iorazutkin.graduatework.exception.FileNotDeleted;
import com.iorazutkin.graduatework.exception.InternalException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {
  @Value("${upload.path}")
  private String uploadPath;

  public String loadFile (MultipartFile file, String catalog) throws IOException {
    if (file == null) {
      throw new BadRequestException();
    }

    File uploadDir = new File(uploadPath);

    if (!uploadDir.exists()) {
      if (!uploadDir.mkdir()) {
        throw new InternalException();
      }
    }

    String uuidFile = UUID.randomUUID().toString();
    String resultFilename = uuidFile + "." + file.getOriginalFilename();

    file.transferTo(new File(uploadPath + "/" + catalog + "/" + resultFilename));

    return resultFilename;
  }

  public void removeFile (String catalog, String oldFilename) {
    if (oldFilename != null) {
      File oldFile = new File(uploadPath + "/" + catalog + "/" + oldFilename);

      if (!oldFile.delete()) {
        throw new FileNotDeleted();
      }
    }
  }
}
