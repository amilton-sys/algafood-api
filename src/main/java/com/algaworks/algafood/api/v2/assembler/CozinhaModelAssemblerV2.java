package com.algaworks.algafood.api.v2.assembler;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.CozinhaController;
import com.algaworks.algafood.api.v1.model.dto.CozinhaModel;
import com.algaworks.algafood.api.v2.model.CozinhaModelV2;
import com.algaworks.algafood.domain.model.Cozinha;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CozinhaModelAssemblerV2 extends RepresentationModelAssemblerSupport<Cozinha, CozinhaModelV2> {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AlgaLinks algaLinks;

    public CozinhaModelAssemblerV2() {
        super(CozinhaController.class, CozinhaModelV2.class);
    }

    @Override
    public CozinhaModelV2 toModel(Cozinha entity) {
        CozinhaModelV2 cozinhaModel = createModelWithId(entity.getId(), entity);

        modelMapper.map(entity, cozinhaModel);

        cozinhaModel.add(algaLinks.linkToCozinhas("cozinhas"));

        return cozinhaModel;
    }
}
