/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.accessRightControl;

/**
 *
 * @author manliqi
 */
public class Right {

    // OES PO
    public static final int canGeneratePO = 1000;
    public static final int canGenerateSO = 1001;
    public static final int canGenerateQuotationAndProductContract = 1002;
    public static final int canGenerateSalesReport = 1003;

    

    //CI systemAdmin
    public static final int canManageUser = 1010;
   

    //MRP 
    public static final int canUseForecast = 1020;
    public static final int canManageProductAndComponent = 1021;
    public static final int canGenerateMRPList = 1022;
    
    

    //CRMS 
    public static final int cananGenerateServicePO = 1030;
    public static final int canUpdateCustomerCredit = 1031;
    public static final int canGenerateServiceSO = 1032;
    public static final int canGenerateQuotationRequest = 1033;
    public static final int canManageServiceCatalog = 1034;
    public static final int canGenerateServiceQuotationAndContract = 1035;
    public static final int canManageKeyAccount = 1036;
    


    //TMS transportation asset
    public static final int canManageTransportationAsset = 1040;
    public static final int canManageTransportationOrder = 1041;
    public static final int canManageLocation = 1042;
    public static final int canManageAssetType = 1043;
    public static final int canUseHRFunction = 1044;
 
    
    //WMS
    public static final int canManageWarehouse = 1050;
    public static final int canManageStockAuditProcess = 1051;
    public static final int canManageStockTransportOrder = 1052;
    public static final int canManageReceivingGoods = 1053;
    public static final int canManageOrderFulfillment = 1054;
    
            
    //GRNS        
    public static final int canManageBid = 1060;
    public static final int canManagePost = 1061;
    
}
