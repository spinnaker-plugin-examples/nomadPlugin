import React from 'react';

import { AccountTag, CollapsibleSection, timestamp } from '@spinnaker/core';
import {INomadInstance} from "../../../domain";


export interface INomadInstanceDetailsSectionProps {
  instance: INomadInstance;
}

export class NomadInstanceDetailsSection extends React.Component<INomadInstanceDetailsSectionProps> {
  constructor(props: INomadInstanceDetailsSectionProps) {
    super(props);
  }

  public render(): JSX.Element {
    const { instance } = this.props;
    return (
      <div>
        <CollapsibleSection heading="Instance Information" defaultExpanded={true}>
          <dl className="dl-horizontal dl-narrow">
            <dt>Launched</dt>
            <dd>{timestamp(instance.launchTime.toString().slice(0, 13))}</dd>
            <dt>In</dt>
            <dd>
              <AccountTag account={instance.account} />
            </dd>
          </dl>
        </CollapsibleSection>
        <CollapsibleSection heading="Status" defaultExpanded={true}>
          <dl className="dl-horizontal dl-narrow">
            <dt>
              <span className={'glyphicon glyphicon-' + instance.healthState + '-triangle'} />
            </dt>
            <dd>{instance.healthState}</dd>

            {instance.details && (
              <div>
                <dt>Details</dt>
                <dd>{instance.details}</dd>
              </div>
            )}
          </dl>
        </CollapsibleSection>
      </div>
    );
  }
}
