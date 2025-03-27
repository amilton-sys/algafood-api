package com.algaworks.algafood.core.modelmapper;

import com.algaworks.algafood.api.v1.model.dto.EnderecoModel;
import com.algaworks.algafood.api.v1.model.dto.input.ItemPedidoInput;
import com.algaworks.algafood.api.v2.model.input.CidadeInputV2;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.ItemPedido;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        var enderecoModelTypeMap = modelMapper.createTypeMap(Endereco.class, EnderecoModel.class);
        enderecoModelTypeMap.<String>addMapping(
                (src) -> src.getCidade().getEstado().getNome(),
                (dest, value) -> dest.getCidade().setEstado(value));

        modelMapper.createTypeMap(CidadeInputV2.class, Cidade.class)
                .addMappings(mapper -> mapper.skip(Cidade::setId));

        modelMapper.createTypeMap(ItemPedidoInput.class, ItemPedido.class)
                .addMappings(mapper -> mapper.skip(ItemPedido::setId));
        return modelMapper;
    }
}

