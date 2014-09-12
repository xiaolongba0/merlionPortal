SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema merlionLogisticDB
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `merlionLogisticDB` ;
CREATE SCHEMA IF NOT EXISTS `merlionLogisticDB` DEFAULT CHARACTER SET utf8 ;
USE `merlionLogisticDB` ;

-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`Company`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`Company` (
  `companyId` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `address` VARCHAR(255) NULL DEFAULT NULL,
  `contactNumber` VARCHAR(45) NULL,
  `contactPersonName` VARCHAR(45) NULL DEFAULT NULL,
  `emailAddress` VARCHAR(45) NULL DEFAULT NULL,
  `description` VARCHAR(45) NULL DEFAULT NULL,
  `package` INT NULL,
  PRIMARY KEY (`companyId`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`UserRole`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`UserRole` (
  `userRoleId` INT(11) NOT NULL AUTO_INCREMENT,
  `roleName` VARCHAR(45) NOT NULL,
  `description` VARCHAR(45) NULL DEFAULT NULL,
  `canGeneratePO` TINYINT(1) NOT NULL DEFAULT 0,
  `canGenerateSO` TINYINT(1) NOT NULL DEFAULT 0,
  `canGenerateQuotationAndProductContract` TINYINT(1) NOT NULL DEFAULT 0,
  `canGenerateSalesReport` TINYINT(1) NOT NULL DEFAULT 0,
  `canManageUser` TINYINT(1) NOT NULL DEFAULT 0,
  `canUseForecast` TINYINT(1) NOT NULL DEFAULT 0,
  `canManageProductAndComponent` TINYINT(1) NOT NULL DEFAULT 0,
  `canGenerateMRPList` TINYINT(1) NOT NULL DEFAULT 0,
  `canGenerateServicePO` TINYINT(1) NOT NULL DEFAULT 0,
  `canUpdateCustomerCredit` TINYINT(1) NOT NULL DEFAULT 0,
  `canGenerateServiceSO` TINYINT(1) NOT NULL DEFAULT 0,
  `canGenerateQuotationRequest` TINYINT(1) NOT NULL DEFAULT 0,
  `canManageServiceCatalog` TINYINT(1) NOT NULL DEFAULT 0,
  `canGenerateServiceQuotationAndContract` TINYINT(1) NOT NULL DEFAULT 0,
  `canManageTransportationAsset` TINYINT(1) NOT NULL DEFAULT 0,
  `canManageTransportationOrder` TINYINT(1) NOT NULL DEFAULT 0,
  `canManageLocation` TINYINT(1) NOT NULL DEFAULT 0,
  `canManageAssetType` TINYINT(1) NOT NULL DEFAULT 0,
  `canUseHRFunction` TINYINT(1) NOT NULL DEFAULT 0,
  `canManageWarehouse` TINYINT(1) NOT NULL DEFAULT 0,
  `canManageStockAuditProcess` TINYINT(1) NOT NULL DEFAULT 0,
  `canManageStockTransportOrder` TINYINT(1) NOT NULL DEFAULT 0,
  `canManageReceivingGoods` TINYINT(1) NOT NULL DEFAULT 0,
  `canManageOrderFulfillment` TINYINT(1) NOT NULL DEFAULT 0,
  `canManageKeyAccount` TINYINT(1) NOT NULL DEFAULT 0,
  `canManageBid` TINYINT(1) NOT NULL DEFAULT 0,
  `canManagePost` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`userRoleId`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`Message`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`Message` (
  `messageId` INT(11) NOT NULL AUTO_INCREMENT,
  `messageTitle` VARCHAR(45) NULL DEFAULT NULL,
  `messageType` VARCHAR(45) NULL DEFAULT NULL,
  `messageBody` VARCHAR(45) NULL DEFAULT NULL,
  `sender` INT NULL DEFAULT NULL,
  `receiver` INT NULL DEFAULT NULL,
  `sentTime` DATETIME NULL DEFAULT NULL,
  `status` INT NULL DEFAULT NULL,
  PRIMARY KEY (`messageId`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`Product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`Product` (
  `productId` INT(11) NOT NULL AUTO_INCREMENT,
  `productName` VARCHAR(45) NOT NULL,
  `description` VARCHAR(45) NULL DEFAULT NULL,
  `productType` VARCHAR(45) NOT NULL,
  `currency` VARCHAR(45) NULL DEFAULT NULL,
  `price` DOUBLE NULL DEFAULT NULL,
  `companyId` INT NULL,
  PRIMARY KEY (`productId`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`SystemUser`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`SystemUser` (
  `systemUserId` INT(11) NOT NULL AUTO_INCREMENT,
  `firstName` VARCHAR(45) NULL,
  `lastName` VARCHAR(45) NULL,
  `emailAddress` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NULL DEFAULT NULL,
  `postalAddress` VARCHAR(45) NULL DEFAULT NULL,
  `contactNumber` VARCHAR(45) NULL DEFAULT NULL,
  `salution` VARCHAR(45) NULL DEFAULT NULL,
  `locked` TINYINT(1) NULL DEFAULT NULL,
  `resetPasswordUponLogin` TINYINT(1) NULL DEFAULT NULL,
  `createdDate` TIMESTAMP NULL DEFAULT NULL,
  `userType` VARCHAR(45) NOT NULL,
  `activated` TINYINT(1) NOT NULL,
  `credit` VARCHAR(45) NULL,
  `Company_companyId` INT(11) NOT NULL,
  PRIMARY KEY (`systemUserId`),
  UNIQUE INDEX `emailAddress_UNIQUE` (`emailAddress` ASC),
  INDEX `fk_SystemUser_Company1_idx` (`Company_companyId` ASC),
  CONSTRAINT `fk_SystemUser_Company1`
    FOREIGN KEY (`Company_companyId`)
    REFERENCES `merlionLogisticDB`.`Company` (`companyId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`ProductOrder`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`ProductOrder` (
  `productPOId` INT(11) NOT NULL AUTO_INCREMENT,
  `createdDate` TIMESTAMP NULL DEFAULT NULL,
  `salesPersonId` VARCHAR(45) NULL DEFAULT NULL,
  `price` DOUBLE NULL DEFAULT NULL,
  `status` INT NULL DEFAULT NULL,
  `shipTo` VARCHAR(45) NULL DEFAULT NULL,
  `billTo` VARCHAR(45) NULL DEFAULT NULL,
  `contactPersonPhoneNumber` VARCHAR(45) NULL DEFAULT NULL,
  `contactPersonName` VARCHAR(45) NULL DEFAULT NULL,
  `systemUserId` INT(11) NOT NULL,
  `creatorId` INT NULL,
  PRIMARY KEY (`productPOId`),
  INDEX `fk_ProductPOHeader_SystemUser1_idx` (`systemUserId` ASC),
  CONSTRAINT `fk_ProductPOHeader_SystemUser1`
    FOREIGN KEY (`systemUserId`)
    REFERENCES `merlionLogisticDB`.`SystemUser` (`systemUserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`SystemLog`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`SystemLog` (
  `logId` INT(11) NOT NULL AUTO_INCREMENT,
  `logTime` TIMESTAMP NULL DEFAULT NULL,
  `action` VARCHAR(255) NULL DEFAULT NULL,
  `SystemUser_systemUserId` INT(11) NOT NULL,
  PRIMARY KEY (`logId`),
  INDEX `fk_SystemLog_SystemUser_idx` (`SystemUser_systemUserId` ASC),
  CONSTRAINT `fk_SystemLog_SystemUser`
    FOREIGN KEY (`SystemUser_systemUserId`)
    REFERENCES `merlionLogisticDB`.`SystemUser` (`systemUserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`Supplier`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`Supplier` (
  `supplierCompanyId` INT NOT NULL AUTO_INCREMENT,
  `contactPerson` VARCHAR(45) NULL,
  `contactNumber` VARCHAR(45) NULL,
  `description` VARCHAR(45) NULL,
  `contactEmail` VARCHAR(45) NULL,
  PRIMARY KEY (`supplierCompanyId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`Component`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`Component` (
  `componentId` INT NOT NULL AUTO_INCREMENT,
  `componentName` VARCHAR(45) NULL,
  `description` VARCHAR(45) NULL,
  `cost` DOUBLE NULL,
  `currency` VARCHAR(45) NULL,
  `Quantity` INT NULL,
  `leadTime` INT NULL,
  `Supplier_supplierCompanyId` INT NOT NULL,
  `Product_productId` INT(11) NOT NULL,
  PRIMARY KEY (`componentId`),
  INDEX `fk_Component_Supplier1_idx` (`Supplier_supplierCompanyId` ASC),
  INDEX `fk_Component_Product1_idx` (`Product_productId` ASC),
  CONSTRAINT `fk_Component_Supplier1`
    FOREIGN KEY (`Supplier_supplierCompanyId`)
    REFERENCES `merlionLogisticDB`.`Supplier` (`supplierCompanyId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Component_Product1`
    FOREIGN KEY (`Product_productId`)
    REFERENCES `merlionLogisticDB`.`Product` (`productId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`SystemUser_has_UserRole`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`SystemUser_has_UserRole` (
  `systemUserId` INT(11) NOT NULL,
  `userRoleId` INT(11) NOT NULL,
  PRIMARY KEY (`systemUserId`, `userRoleId`),
  INDEX `fk_SystemUser_has_UserRole_UserRole1_idx` (`userRoleId` ASC),
  INDEX `fk_SystemUser_has_UserRole_SystemUser1_idx` (`systemUserId` ASC),
  CONSTRAINT `fk_SystemUser_has_UserRole_SystemUser1`
    FOREIGN KEY (`systemUserId`)
    REFERENCES `merlionLogisticDB`.`SystemUser` (`systemUserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SystemUser_has_UserRole_UserRole1`
    FOREIGN KEY (`userRoleId`)
    REFERENCES `merlionLogisticDB`.`UserRole` (`userRoleId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`ProductOrderLineItem`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`ProductOrderLineItem` (
  `line` INT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(45) NULL,
  `quantity` INT NULL,
  `Product_productId` INT(11) NOT NULL,
  `ProductOrder_productPOId` INT(11) NOT NULL,
  INDEX `fk_ProductOrderLineItem_Product1_idx` (`Product_productId` ASC),
  INDEX `fk_ProductOrderLineItem_ProductOrder1_idx` (`ProductOrder_productPOId` ASC),
  PRIMARY KEY (`line`),
  CONSTRAINT `fk_ProductOrderLineItem_Product1`
    FOREIGN KEY (`Product_productId`)
    REFERENCES `merlionLogisticDB`.`Product` (`productId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ProductOrderLineItem_ProductOrder1`
    FOREIGN KEY (`ProductOrder_productPOId`)
    REFERENCES `merlionLogisticDB`.`ProductOrder` (`productPOId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`ProductContract`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`ProductContract` (
  `productContractId` INT NOT NULL AUTO_INCREMENT,
  `contractTerm` VARCHAR(255) NULL,
  `status` INT NULL,
  `validity` VARCHAR(45) NULL,
  `createDate` TIMESTAMP NULL,
  `creatorId` VARCHAR(45) NULL,
  `discountRate` VARCHAR(255) NULL,
  `clientId` INT NOT NULL,
  `companyId` INT(11) NOT NULL,
  `signedContract` BLOB NULL,
  `quotationId` INT NOT NULL,
  PRIMARY KEY (`productContractId`),
  INDEX `fk_ProductContract_Company1_idx` (`companyId` ASC),
  CONSTRAINT `fk_ProductContract_Company1`
    FOREIGN KEY (`companyId`)
    REFERENCES `merlionLogisticDB`.`Company` (`companyId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`Company_has_UserRole`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`Company_has_UserRole` (
  `companyId` INT(11) NOT NULL,
  `userRoleId` INT(11) NOT NULL,
  PRIMARY KEY (`companyId`, `userRoleId`),
  INDEX `fk_Company_has_UserRole_UserRole1_idx` (`userRoleId` ASC),
  INDEX `fk_Company_has_UserRole_Company1_idx` (`companyId` ASC),
  CONSTRAINT `fk_Company_has_UserRole_Company1`
    FOREIGN KEY (`companyId`)
    REFERENCES `merlionLogisticDB`.`Company` (`companyId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Company_has_UserRole_UserRole1`
    FOREIGN KEY (`userRoleId`)
    REFERENCES `merlionLogisticDB`.`UserRole` (`userRoleId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`Quotation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`Quotation` (
  `quotationId` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NULL,
  `customerId` INT NULL,
  `status` INT NULL,
  `createDate` TIMESTAMP NULL,
  `company` INT NULL,
  PRIMARY KEY (`quotationId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`QuotationLineItem`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`QuotationLineItem` (
  `line` INT NOT NULL AUTO_INCREMENT,
  `lineItemPrice` DOUBLE NULL,
  `Quotation_quotationId` INT NOT NULL,
  `Product_productId` INT(11) NOT NULL,
  PRIMARY KEY (`line`),
  INDEX `fk_QuotationLineItem_Quotation1_idx` (`Quotation_quotationId` ASC),
  INDEX `fk_QuotationLineItem_Product1_idx` (`Product_productId` ASC),
  CONSTRAINT `fk_QuotationLineItem_Quotation1`
    FOREIGN KEY (`Quotation_quotationId`)
    REFERENCES `merlionLogisticDB`.`Quotation` (`quotationId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_QuotationLineItem_Product1`
    FOREIGN KEY (`Product_productId`)
    REFERENCES `merlionLogisticDB`.`Product` (`productId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`Location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`Location` (
  `locationId` INT NOT NULL,
  `locationName` VARCHAR(45) NULL,
  `locationPosition` VARCHAR(255) NULL,
  `ProductContract_productContractId` INT NOT NULL,
  PRIMARY KEY (`locationId`),
  INDEX `fk_Location_ProductContract1_idx` (`ProductContract_productContractId` ASC),
  CONSTRAINT `fk_Location_ProductContract1`
    FOREIGN KEY (`ProductContract_productContractId`)
    REFERENCES `merlionLogisticDB`.`ProductContract` (`productContractId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`TransporationAsset`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`TransporationAsset` (
  `assetId` INT NOT NULL,
  `capacity` VARCHAR(45) NULL,
  `assetType` VARCHAR(45) NULL,
  `price` INT NULL,
  `speed` INT NULL,
  `status` VARCHAR(45) NULL,
  `isAvailable` TINYINT(1) NULL,
  `isMaintain` TINYINT(1) NULL,
  `routeId` INT NULL,
  `Location_locationId` INT NOT NULL,
  PRIMARY KEY (`assetId`),
  INDEX `fk_TransporationAsset_Location1_idx` (`Location_locationId` ASC),
  CONSTRAINT `fk_TransporationAsset_Location1`
    FOREIGN KEY (`Location_locationId`)
    REFERENCES `merlionLogisticDB`.`Location` (`locationId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`TransportationOrder`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`TransportationOrder` (
  `transportationOrderId` INT NOT NULL,
  `companyId` INT NULL,
  `creatorId` INT NULL,
  `referenceId` INT NULL,
  `referenceType` INT NULL,
  `origin` VARCHAR(45) NULL,
  `destination` VARCHAR(45) NULL,
  `timeStart` VARCHAR(45) NULL,
  `timeEnd` VARCHAR(45) NULL,
  `cargoType` VARCHAR(45) NULL,
  `cargoWeight` VARCHAR(45) NULL,
  PRIMARY KEY (`transportationOrderId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`TransportationLog`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`TransportationLog` (
  `logId` INT NOT NULL,
  `action` VARCHAR(255) NULL,
  `timeStamp` TIMESTAMP NULL,
  `TransportationOrder_transportationOrderId` INT NOT NULL,
  PRIMARY KEY (`logId`),
  INDEX `fk_TransportationLog_TransportationOrder1_idx` (`TransportationOrder_transportationOrderId` ASC),
  CONSTRAINT `fk_TransportationLog_TransportationOrder1`
    FOREIGN KEY (`TransportationOrder_transportationOrderId`)
    REFERENCES `merlionLogisticDB`.`TransportationOrder` (`transportationOrderId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`Route`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`Route` (
  `routeId` INT NOT NULL,
  `routeType` VARCHAR(45) NULL,
  `origin` VARCHAR(45) NULL,
  `destination` VARCHAR(45) NULL,
  `distance` VARCHAR(45) NULL,
  PRIMARY KEY (`routeId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`AssetSchedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`AssetSchedule` (
  `scheduleId` INT NOT NULL,
  `startDate` VARCHAR(45) NULL,
  `endDate` VARCHAR(45) NULL,
  `TransporationAsset_assetId` INT NOT NULL,
  PRIMARY KEY (`scheduleId`),
  INDEX `fk_AssetSchedule_TransporationAsset1_idx` (`TransporationAsset_assetId` ASC),
  CONSTRAINT `fk_AssetSchedule_TransporationAsset1`
    FOREIGN KEY (`TransporationAsset_assetId`)
    REFERENCES `merlionLogisticDB`.`TransporationAsset` (`assetId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`TransportationOperator`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`TransportationOperator` (
  `operatorId` INT NOT NULL,
  `isAvailable` TINYINT(1) NULL,
  `operatorStatus` VARCHAR(45) NULL,
  `operatorType` VARCHAR(45) NULL,
  `AssetSchedule_scheduleId` INT NOT NULL,
  PRIMARY KEY (`operatorId`),
  INDEX `fk_TransportationOperator_AssetSchedule1_idx` (`AssetSchedule_scheduleId` ASC),
  CONSTRAINT `fk_TransportationOperator_AssetSchedule1`
    FOREIGN KEY (`AssetSchedule_scheduleId`)
    REFERENCES `merlionLogisticDB`.`AssetSchedule` (`scheduleId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`OperatorSchedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`OperatorSchedule` (
  `operatorScheduleId` INT NOT NULL,
  `startDate` VARCHAR(45) NULL,
  `endDate` VARCHAR(45) NULL,
  `TransportationOperator_operatorId` INT NOT NULL,
  PRIMARY KEY (`operatorScheduleId`),
  INDEX `fk_OperatorSchedule_TransportationOperator1_idx` (`TransportationOperator_operatorId` ASC),
  CONSTRAINT `fk_OperatorSchedule_TransportationOperator1`
    FOREIGN KEY (`TransportationOperator_operatorId`)
    REFERENCES `merlionLogisticDB`.`TransportationOperator` (`operatorId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`MaintenanceLog`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`MaintenanceLog` (
  `maintenanceLogId` INT NOT NULL,
  `operatorId` VARCHAR(45) NULL,
  `cost` VARCHAR(45) NULL,
  `description` VARCHAR(45) NULL,
  `TransporationAsset_assetId` INT NOT NULL,
  PRIMARY KEY (`maintenanceLogId`),
  INDEX `fk_MaintenanceLog_TransporationAsset1_idx` (`TransporationAsset_assetId` ASC),
  CONSTRAINT `fk_MaintenanceLog_TransporationAsset1`
    FOREIGN KEY (`TransporationAsset_assetId`)
    REFERENCES `merlionLogisticDB`.`TransporationAsset` (`assetId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`TransporationAsset_has_TransportationOrder`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`TransporationAsset_has_TransportationOrder` (
  `TransporationAsset_assetId` INT NOT NULL,
  `TransportationOrder_transportationOrderId` INT NOT NULL,
  PRIMARY KEY (`TransporationAsset_assetId`, `TransportationOrder_transportationOrderId`),
  INDEX `fk_TransporationAsset_has_TransportationOrder_Transportatio_idx` (`TransportationOrder_transportationOrderId` ASC),
  INDEX `fk_TransporationAsset_has_TransportationOrder_Transporation_idx` (`TransporationAsset_assetId` ASC),
  CONSTRAINT `fk_TransporationAsset_has_TransportationOrder_TransporationAs1`
    FOREIGN KEY (`TransporationAsset_assetId`)
    REFERENCES `merlionLogisticDB`.`TransporationAsset` (`assetId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TransporationAsset_has_TransportationOrder_TransportationO1`
    FOREIGN KEY (`TransportationOrder_transportationOrderId`)
    REFERENCES `merlionLogisticDB`.`TransportationOrder` (`transportationOrderId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `merlionLogisticDB`.`TransporationAsset_has_Route`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `merlionLogisticDB`.`TransporationAsset_has_Route` (
  `TransporationAsset_assetId` INT NOT NULL,
  `Route_routeId` INT NOT NULL,
  PRIMARY KEY (`TransporationAsset_assetId`, `Route_routeId`),
  INDEX `fk_TransporationAsset_has_Route_Route1_idx` (`Route_routeId` ASC),
  INDEX `fk_TransporationAsset_has_Route_TransporationAsset1_idx` (`TransporationAsset_assetId` ASC),
  CONSTRAINT `fk_TransporationAsset_has_Route_TransporationAsset1`
    FOREIGN KEY (`TransporationAsset_assetId`)
    REFERENCES `merlionLogisticDB`.`TransporationAsset` (`assetId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TransporationAsset_has_Route_Route1`
    FOREIGN KEY (`Route_routeId`)
    REFERENCES `merlionLogisticDB`.`Route` (`routeId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
