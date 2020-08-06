import { IPromise } from 'angular';

import {INomadServerGroup} from "../domain";

export class NomadServerGroupTransformer {
  public static $inject = ['$q'];
  public constructor(private $q: ng.IQService) {}

  public normalizeServerGroupDetails(serverGroup: INomadServerGroup): INomadServerGroup {
    return serverGroup;
  }

  public normalizeServerGroup(serverGroup: INomadServerGroup): IPromise<INomadServerGroup> {
    return this.$q.resolve(serverGroup);
  }

}
