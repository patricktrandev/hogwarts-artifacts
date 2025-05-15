package com.cloudinfo.hogwartsartifact.wizard.converter;

import com.cloudinfo.hogwartsartifact.wizard.Wizard;
import com.cloudinfo.hogwartsartifact.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardDtoMapper implements Converter<WizardDto, Wizard> {
    @Override
    public Wizard convert(WizardDto source) {
        Wizard w= new Wizard();
        w.setName(source.name());
        w.setId(source.id());
        return w;
    }
}
