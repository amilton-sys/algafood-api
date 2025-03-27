package com.algaworks.algafood.api.v1.model.dto.input;

import com.algaworks.algafood.core.validation.FileContentType;
import com.algaworks.algafood.core.validation.FileSize;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FotoProdutoInput {
    @NotNull
    @FileSize(max = "500KB")
    @FileContentType(allowed = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    private MultipartFile arquivo;
    @NotBlank
    private String descricao;
}
