/*!
 * Copyright 2010 - 2017 Hitachi Vantara.  All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.pentaho.di.ui.repository.pur.repositoryexplorer.model;

import java.util.List;

import org.pentaho.ui.xul.util.AbstractModelNode;

public class UIRepositoryObjectRevisions extends AbstractModelNode<UIRepositoryObjectRevision> implements
    java.io.Serializable {

  private static final long serialVersionUID = -5243106180726024567L; /* EESOURCE: UPDATE SERIALVERUID */

  public UIRepositoryObjectRevisions() {
  }

  public UIRepositoryObjectRevisions( List<UIRepositoryObjectRevision> revisions ) {
    super( revisions );
  }

  @Override
  protected void fireCollectionChanged() {
    this.changeSupport.firePropertyChange( "children", null, this );
  }

}
