import React from 'react';

import {
  CloudProviderRegistry,
} from '@spinnaker/core';

import {NomadServerGroupActions, nomadServerGroupDetailsGetter, NomadServerGroupTransformer} from "./serverGroup";
import {NomadInstanceDetails} from "./instance/details";
import {ServerGroupInformationSection} from "./serverGroup/details/sections/ServerGroupInformationSection";
import {ServerGroupJobSection} from "./serverGroup/details/sections/ServerGroupJobSection";
import {ServerGroupTaskGroupSection} from "./serverGroup/details/sections/ServerGroupTaskGroupSection";
import {ServerGroupTasksSection} from "./serverGroup/details/sections/ServerGroupTasksSection";

export const initialize = () => {
  CloudProviderRegistry.registerProvider('nomad', {
    name: 'Nomad',
    logo: {
      path: './logo/nomad.png',
    },
    serverGroup: {
      skipUpstreamStageCheck: true,
      transformer: NomadServerGroupTransformer,
      detailsActions: NomadServerGroupActions,
      detailsGetter: nomadServerGroupDetailsGetter,
      detailsSections: [
        ServerGroupInformationSection,
        ServerGroupJobSection,
        ServerGroupTaskGroupSection,
        ServerGroupTasksSection,
      ],
      scalingActivitiesEnabled: false,
    },
    instance: {
      details: NomadInstanceDetails,
    },
  });
};

