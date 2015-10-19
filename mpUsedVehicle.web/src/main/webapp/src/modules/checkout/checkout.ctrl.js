(function (ng) {
    var mod = ng.module('checkoutModule');

    mod.controller('checkoutCtrl', ['CrudCreator', '$scope', 'checkoutService', 'cartItemModel', '$location', 'authService', '$timeout', 'stBlurredDialog', 'userService', function (CrudCreator, $scope, svc, model, $location, authSvc, $timeout, stBlurredDialog, svcUser) {
            CrudCreator.extendController(this, svc, $scope, model, 'cartItem', 'My Shopping Cart');
            var self = this;

            var oldFetch = this.fetchRecords;
            this.fetchRecords = function () {
                return oldFetch.call(this).then(function (data) {
                    self.calcTotal();
                    return data;
                });
            };
            this.fetchRecords();
            this.readOnly = true;
            $scope.lastQuantity = 0;
            $scope.total = 0;
            $scope.taxes = 0;
            $scope.subtotal = 0;
            $scope.credit = true;
            $scope.confirm = false;

            this.recordActions = {
                delete: {
                    displayName: ' ',
                    icon: 'trash',
                    class: 'primary',
                    fn: function (record) {
                        svc.deleteRecord(record).then(function () {
                            self.fetchRecords();
                        });
                    },
                    show: function () {
                        return true;
                    }
                }};
            
            this.calcTotal = function () {
                $scope.total = 0;
                $scope.taxes = 0;
                $scope.subtotal = 0;
                var order = $scope.records[0];
                for (var i = 0; i < order.cartItems.length; i++) {
                    $scope.subtotal += order.cartItems[i].product.price;
                    $scope.taxes += (order.cartItems[i].product.price * 0.16);
                }
                $scope.total = $scope.subtotal + $scope.taxes;
            };

            $scope.verify = function (quantity) {
                $scope.lastQuantity = quantity;
            };//guarda la cantidad anterior

            $scope.postVerify = function (record) {
                var patron = /^\d*$/; //^[0-9]{3}$
                if (patron.test(record.quantity) && record.quantity > 0) {
                    self.calcTotal();
                } else {
                    self.showError("You must enter a valid quantity");
                    record.quantity = $scope.lastQuantity;
                    $scope.currentRecord = record;
                }
            };//Realiza la validacion de la nueva cantidad asignada.
            
            
            
            $scope.selectingMethod = true;
            
            $scope.paymentMethods = [{
                "type": "CREDIT_CARD",
                "agreements": [{
                    "name": "VISA",
                    "description": "Visa",
                    "template": "src/modules/checkout/templates/credit.html",
                    "logo": "images/visa_logo_6.gif"
                },
                {
                    "name": "MASTER_CARD",
                    "description": "Master Card",
                    "template": "src/modules/checkout/templates/credit.html",
                    "logo": "images/MasterCard_logo.png"
                }]
            },
            {
                "type": "OTHERS",
                "agreements": [{
                    "name": "PSE",
                    "description": "PSE",
                    "template": "src/modules/checkout/templates/pse.html",
                    "logo": "images/PSE-logo.png"
                },
                {
                    "name": "PAYPAL",
                    "description": "PayPal",
                    "template": "src/modules/checkout/templates/paypal.html",
                    "logo": "images/Paypal_logo-5.png"
                },{
                
                    "name": "DEBIT",
                    "description": "Debit",
                    "template": "src/modules/checkout/templates/debit.html",
                    "logo": "images/Maestro_logo.png"
                }]
            }];
            
            //cambia el metodo de pago
            $scope.selectPaymentMethodAgreement = function(method){
                $scope.selectedAgreement = method;
                $scope.selectingMethod = false;
                $scope.templateUrl = $scope.selectedAgreement.template ;
            };
            
            $scope.modifySelection = function(){
                $scope.selectingMethod = !$scope.selectingMethod;
            };
            if(authSvc.getCurrentUser()=== undefined || authSvc.getCurrentUser()=== null)
                {
                    $location.path('/login');
                }else{
                    svcUser.api.one('currentUser').get().then(function(user) {
                        console.log(user.role);
                        $scope.role=user.role;
                        if($scope.role==="provider"){
                           svc.getOrderByProvider(authSvc.getCurrentUser().id).then(function (result) {
                                $scope.providerOrders = [];
                                $scope.providerOrders = result;
                            });
                        }else{
                            if($location.path() === '/listorders')
                            {
                                $location.path('/myorders');
                            }else{
                                svc.getOrderByClient(authSvc.getCurrentUser().id).then(function (result) {
                                    $scope.orders = [];
                                    $scope.orders = result;
                                });
                            }
                        }
                    });
            }
            $scope.modalEdit = function(option){
                $scope.orderEdited = $scope.providerOrders[option];
                $('#editOrder').modal('show');
                return false;
            };
            
            $scope.saveStatus = function(){
                svc.saveStatus($scope.orderEdited);
                $('#editOrder').modal('hide');
                return false;
            };
            
            $scope.options = [
                {
                  name: 'Merchant Confirmed',
                  value: 'Merchant Confirmed'
                }, 
                {
                  name: 'Shipped',
                  value: 'Shipped'
                }, 
                {
                  name: 'Completed',
                  value: 'Completed'
                }, 
                {
                  name: 'Canceled',
                  value: 'Canceled'
                },
                {
                  name: 'AUTHORIZED',
                  value: 'AUTHORIZED'
                }
                
            ];
           
            
            $scope.toggleConfirmation = function(){
                $scope.confirm = !$scope.confirm;
                $('#confirmationModal').modal('show');
            };
            
            $scope.pay = function () {
                $scope.records[0].amount = $scope.subtotal;
                $scope.records[0].taxAmount = $scope.taxes;
                $scope.records[0].amountWithTaxes = $scope.total;
                $scope.records[0].orderStatus = 'AUTHORIZED';
                $scope.records[0].paymentMethod = $scope.selectedAgreement.name;
                $scope.confirm = !$scope.confirm;
                $('#confirmationModal').modal('hide');
                svc.saveOrder($scope.records[0]);
                
                stBlurredDialog.open('src/modules/checkout/templates/processing.html', {msg: 'Thank you for buying at Hotwheels, you will be redirected!'});
                
            };
            
            
        }]);


    // Create a controller for your modal dialog
    mod.controller('DialogCtrl', ['$scope', 'stBlurredDialog', '$timeout', '$location', function($scope, stBlurredDialog, $timeout, $location){
        // Get the data passed from the controller
        $scope.dialogData = stBlurredDialog.getDialogData();
        
        $scope.processing = true;
        $timeout(function(){
            $scope.processing = false;
        }, 3000);
        
        $scope.finishOperation = function(){
            stBlurredDialog.close();
            $location.path('/');
        }
        
    }]);
})(window.angular);
