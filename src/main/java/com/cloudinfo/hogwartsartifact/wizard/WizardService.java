package com.cloudinfo.hogwartsartifact.wizard;

import java.util.List;

public interface WizardService {
    Wizard createWizard(Wizard w);
    Wizard findWizardByid(String id);
  List<Wizard> findAllWizards();
    void deleteWizard(String id);
    Wizard updateWizard(String id, Wizard convert);

    Wizard assignToWizard(String id, String artifactId);

    Wizard registerCourse(String id, String courseId);
}
