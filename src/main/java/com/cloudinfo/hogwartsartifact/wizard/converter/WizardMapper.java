package com.cloudinfo.hogwartsartifact.wizard.converter;

import com.cloudinfo.hogwartsartifact.wizard.Wizard;
import com.cloudinfo.hogwartsartifact.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardMapper implements Converter<Wizard, WizardDto> {
    @Override
    public WizardDto convert(Wizard source) {
        WizardDto wizardDto= new WizardDto(source.getId(),source.getName(),source.getNumberOfArtifacts());
        return wizardDto;
    }
}
