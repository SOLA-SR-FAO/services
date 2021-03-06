/**
 * ******************************************************************************************
 * Copyright (C) 2015 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.sola.services.ejb.cadastre.businesslogic;

import java.util.List;
import javax.ejb.Local;
import org.sola.services.common.ejbs.AbstractEJBLocal;
import org.sola.services.ejb.cadastre.repository.entities.*;

/**
 * Local interface for the {@linkplain CadastreEJB}
 */
@Local
public interface CadastreEJBLocal extends AbstractEJBLocal {

    /**
     * See {@linkplain CadastreEJB#getLandUseTypes(java.lang.String)
     * CadastreEJB.getCadastreObjectTypes}.
     */
    List<LandUseType> getLandUseTypes(String languageCode);

    /**
     * See {@linkplain CadastreEJB#getCadastreObjectTypes(java.lang.String)
     * CadastreEJB.getCadastreObjectTypes}.
     */
    List<CadastreObjectType> getCadastreObjectTypes(String languageCode);

    /**
     * See {@linkplain CadastreEJB#getCadastreObject(java.lang.String)
     * CadastreEJB.getCadastreObject}.
     */
    CadastreObject getCadastreObject(String id);

    /**
     * See {@linkplain CadastreEJB#getCadastreObjects(java.util.List)
     * CadastreEJB.getCadastreObjects}.
     */
    List<CadastreObject> getCadastreObjects(List<String> cadastreObjIds);

    /**
     * See {@linkplain CadastreEJB#getCadastreObjectByParts(java.lang.String)
     * CadastreEJB.getCadastreObjectByParts}.
     */
    List<CadastreObject> getCadastreObjectByParts(String searchString);

    /**
     * See {@linkplain CadastreEJB#getCadastreObjectByAllParts(java.lang.String)
     * CadastreEJB.getCadastreObjectByAllParts}.
     */
    List<CadastreObject> getCadastreObjectByAllParts(String searchString);

    /**
     * See {@linkplain CadastreEJB#saveCadastreObject(org.sola.services.ejb.cadastre.repository.entities.CadastreObject)
     * CadastreEJB.saveCadastreObject}.
     */
    CadastreObject saveCadastreObject(CadastreObject cadastreObject);

    /**
     * See {@linkplain CadastreEJB#getCadastreObjectByPoint(double, double, int)
     * CadastreEJB.getCadastreObjectByPoint}.
     */
    CadastreObject getCadastreObjectByPoint(double x, double y, int srid, String typeCode);

    /**
     * See {@linkplain CadastreEJB#getCadastreObjectsByBaUnit(java.lang.String)
     * CadastreEJB.getCadastreObjectsByBaUnit}.
     */
    List<CadastreObject> getCadastreObjectsByBaUnit(String baUnitId);

    /**
     * See {@linkplain CadastreEJB#getCadastreObjectsByService(java.lang.String)
     * CadastreEJB.getCadastreObjectsByService}.
     */
    List<CadastreObject> getCadastreObjectsByService(String serviceId);

    /**
     * See {@linkplain CadastreEJB#getCadastreObjectTargetsByTransaction(java.lang.String)
     * CadastreEJB.getCadastreObjectTargetsByTransaction}.
     */
    List<CadastreObjectTarget> getCadastreObjectTargetsByTransaction(String transactionId);

    /**
     * See {@linkplain CadastreEJB#getSurveyPointsByTransaction(java.lang.String)
     * CadastreEJB.getSurveyPointsByTransaction}.
     */
    List<SurveyPoint> getSurveyPointsByTransaction(String transactionId);

    /**
     * See {@linkplain CadastreEJB#getCadastreObjectsByTransaction(java.lang.String)
     * CadastreEJB.getCadastreObjectsByTransaction}.
     */
    List<CadastreObject> getCadastreObjectsByTransaction(String transactionId);

    /**
     * See {@linkplain CadastreEJB#ChangeStatusOfCadastreObjects(java.lang.String, java.lang.String, java.lang.String)
     * CadastreEJB.ChangeStatusOfCadastreObjects}.
     */
    void ChangeStatusOfCadastreObjects(String transactionId, String filter, String statusCode);

    /**
     * See {@linkplain CadastreEJB#getCadastreObjectNode(double, double, double, double, int)
     * CadastreEJB.getCadastreObjectNode}.
     */
    CadastreObjectNode getCadastreObjectNode(
            double xMin, double yMin, double xMax, double yMax, int srid, String cadastreObjectType);

    /**
     * See {@linkplain CadastreEJB#getCadastreObjectNodePotential(double, double, double, double, int)
     * CadastreEJB.getCadastreObjectNodePotential}.
     */
    CadastreObjectNode getCadastreObjectNodePotential(
            double xMin, double yMin, double xMax, double yMax, int srid, String cadastreObjectType);

    /**
     * See {@linkplain CadastreEJB#getCadastreObjectNodeTargetsByTransaction(java.lang.String)
     * CadastreEJB.getCadastreObjectNodeTargetsByTransaction}.
     */
    List<CadastreObjectNodeTarget> getCadastreObjectNodeTargetsByTransaction(String transactionId);

    /**
     * See {@linkplain CadastreEJB#getCadastreObjectRedefinitionTargetsByTransaction(java.lang.String)
     * CadastreEJB.getCadastreObjectRedefinitionTargetsByTransaction}.
     */
    List<CadastreObjectTargetRedefinition> getCadastreObjectRedefinitionTargetsByTransaction(
            String transactionId);

    /**
     * See {@linkplain CadastreEJB#approveCadastreRedefinition(java.lang.String)
     * CadastreEJB.approveCadastreRedefinition}.
     */
    void approveCadastreRedefinition(String transactionId);

    /**
     * See {@linkplain CadastreEJB#getSpatialUnitTemporaryListByTransaction(java.lang.String)
     * CadastreEJB.getSpatialUnitTemporaryListByTransaction}.
     */
    List<SpatialUnitTemporary> getSpatialUnitTemporaryListByTransaction(
            String transactionId);
    
    /**
     * see {@linkplain CadastreEJB#getSpatialValueArea(java.lang.String)
     * CadastreEJB.getSpatialValueArea}
     */
    public SpatialValueArea getSpatialValueArea(String colist);

    /**
     * see {@linkplain CadastreEJB#getNewCadastreObjectIdentifier(byte[], java.lang.String)
     * CadastreEJB.getNewCadastreObjectIdentifier}
     */
   NewCadastreObjectIdentifier getNewCadastreObjectIdentifier(byte[] geom, String cadastreObjectType);
   
     /**
     * See {@linkplain CadastreEJB#getCadastreObjectByParts(java.lang.String)
     * CadastreEJB.getCadastreObjectByParts}.
     */
    List<SpatialUnitGroup> getSpatialUnitGroupByParts(String searchString);
     /**
     * See {@linkplain CadastreEJB#getCadastreObjectByParts(java.lang.String)
     * CadastreEJB.getCadastreObjectByParts}.
     */
    List<SpatialUnitGroup> getSpatialUnitGroupByHierarchy(String searchString, Integer hierarchyLevel);

    /**
     * See {@linkplain CadastreEJB#getCadastreObjectByAllParts(java.lang.String)
     * CadastreEJB.getCadastreObjectByAllParts}.
     */
    List<SpatialUnitGroup> getSpatialUnitGroupByAllParts(String searchString);

   
     /**
     * see {@linkplain CadastreEJB#saveSpatialUnitGroups(List<SpatialUnitGroup>, String)
     * CadastreEJB.saveSpatialUnitGroups}
     */
   void saveSpatialUnitGroups(List<SpatialUnitGroup> items, String languageCode);
   
    /**
     * see {@linkplain CadastreEJB#getSpatialUnitGroups(byte[], Integer, Integer)
     * CadastreEJB.getSpatialUnitGroups}
     */
   List<SpatialUnitGroup> getSpatialUnitGroups(
           byte[] filteringGeometry, Integer hierarchyLevel, Integer srid);

    /**
     * See {@linkplain CadastreEJB#getSpatialUnitGroupsByIds(java.util.List)
     * CadastreEJB.getSpatialUnitGroupsByIds}.
     */
    List<SpatialUnitGroup> getSpatialUnitGroupsByIds(List<String> ids);
    
        /**
     * See {@linkplain CadastreEJB#getHierarchyLevels(java.lang.String)
     * CadastreEJB.getHierarchyLevels}.
     */
    List<HierarchyLevel> getHierarchyLevels(String languageCode);
    
    
     /**
     * See {@linkplain CadastreEJB#getSysRegWorkUnitByAllParts(java.lang.String)
     * CadastreEJB.getSysRegWorkUnitByAllParts}.
     */
    SysRegWorkUnit getSysRegWorkUnitByAllParts(String searchString);

     /**
     * See {@linkplain CadastreEJB#getSysRegWorkUnitByIds(java.util.List)
     * CadastreEJB.getSysRegWorkUnitByIds}.
     */
    SysRegWorkUnit getSysRegWorkUnitByIds(String id);
    
     /**
     * see {@linkplain CadastreEJB#saveSSysRegWorkUnit(List<SysRegWorkUnit, String)
     * CadastreEJB.saveSysRegWorkUnit}
     */
    SysRegWorkUnit saveSysRegWorkUnit(SysRegWorkUnit items, String languageCode);
   
     /**
     * See {@linkplain CadastreEJB#getLevels(java.lang.String)
     * CadastreEJB.getLevels}.
     */
    List<Level> getLevels(String languageCode);
    
    /**
     * see {@linkplain CadastreEJB#getSpatialUnits(byte[], String, Integer)
     * CadastreEJB.getSpatialUnits}
     */
   List<SpatialUnit> getSpatialUnits(
           byte[] filteringGeometry, String levelId, Integer srid);

    /**
     * see {@linkplain CadastreEJB#saveSpatialUnits(List<SpatialUnit>, String)
     * CadastreEJB.saveSpatialUnits}
     */
   void saveSpatialUnits(List<SpatialUnit> items, String languageCode);

    /**
     * See {@linkplain CadastreEJB#getSpatialUnitsByIds(java.util.List)
     * CadastreEJB.getSpatialUnitsByIds}.
     */
    List<SpatialUnit> getSpatialUnitsByIds(List<String> ids);

}

