(function (ng) {
    var mod = ng.module('productModule');

    //Provider Products Controller
    mod.controller('productsCtrl', ['CrudCreator', '$scope', 'productService', 'productModel', function (CrudCreator, $scope, svc, model) {
            CrudCreator.extendController(this, svc, $scope, model, 'product', 'Product');
            this.fetchRecords();
            this.loadRefOptions();
        }]);
    
    //User/Buyer Controller
    mod.controller('productCtrl', ['CrudCreator', '$scope', 'productService', 'productModel', 
        'cartItemService', 'messageService','commentService','$location', 'authService', 'vehicleService', 
        'userService', 
        function (CrudCreator, $scope, svc, model, cartItemSvc, messageSvc, commentSvc, $location, authSvc, vehicleSvc, userSvc) {
            CrudCreator.extendController(this, svc, $scope, model, 'product', 'Products');
            //Variables
            $scope.varEnable = true;
            $scope.providerName = ''; 
            $scope.records=[];
            $scope.vehiclesRecords=[];
            $scope.vehicleNames=[];
            $scope.vehicleBrands=[];
            $scope.vehicleCapacities=[];
            $scope.vehicleColors=[];
            $scope.vehicleModels=[];
            $scope.vehiclePlates=[];
            $scope.vehicleBrands=[];
            $scope.text2Search=""; 
            
            // Vars for advanced search
            $scope.brandFilter = "";
            $scope.modelFilter = "";
            $scope.capacityFilter = "";
            $scope.priceFilter = "";
            $scope.colorFilter = "";
            $scope.plateFilter = "";
            $scope.locationFilter = "";
            
            //Funciones
            $scope.getFilters = function(){
                svc.getVehiclesName().then(function (products) {
                    $scope.vehicleNames = [];
                    for (var i = 0; i < products.length; i++) {
                           $scope.vehicleNames.push(products[i]);                   
                    }
                });
                 svc.getVehiclesBrand().then(function (products) {
                    $scope.vehicleBrands = [];
                    for (var i = 0; i < products.length; i++) {
                           $scope.vehicleBrands.push(products[i]);                   
                    }
                });
                 svc.getVehiclesCapacity().then(function (products) {
                    $scope.vehicleCapacities = [];
                    for (var i = 0; i < products.length; i++) {
                           $scope.vehicleCapacities.push(products[i]);                   
                    }
                });
                 svc.getVehiclesColor().then(function (products) {
                    $scope.vehicleColors = [];
                    for (var i = 0; i < products.length; i++) {
                           $scope.vehicleColors.push(products[i]);                   
                    }
                });      
                 svc.getVehiclesModel().then(function (products) {
                    $scope.vehicleModels = [];
                    for (var i = 0; i < products.length; i++) {
                           $scope.vehicleModels.push(products[i]);                   
                    }
                });   
                svc.getVehiclesPlate().then(function (products) {
                    $scope.vehiclePlates = [];
                    for (var i = 0; i < products.length; i++) {
                           $scope.vehiclePlates.push(products[i]);                   
                    }
                });   
                 svc.getVehiclesLocation().then(function (products) {
                    $scope.vehicleLocations = [];
                    for (var i = 0; i < products.length; i++) {
                           $scope.vehicleLocations.push(products[i]);                   
                    }
                });                  
            }
            $scope.getProductsByAdvancedSearch = function () {
                 
                svc.getProductsByAdvancedSearch(
                        $scope.brandFilter, $scope.modelFilter,
                        $scope.capacityFilter, $scope.priceFilter,
                        $scope.plateFilter,$scope.locationFilter,
                        $scope.colorFilter).then(function (products) {
                            
                    $scope.records = [];
                    for (var i = 0; i < products.length; i++) {
                        $scope.records.push(products[i]);
                        
                    }
                });
            };
            
            $scope.findItem = function () {
                console.log("$scope.records" + $scope.records.length);
                console.log("Ingresa text2Search" + $scope.text2Search);
                if ($scope.searchCriteria == "byProvider")
                {
                    console.log("searchCriteria byProvider" + $scope.searchCriteria);
                    svc.findCheaperbyProvider($scope.text2Search).then(function (Cheaperprovider) {
                        $scope.records = [];
                        $scope.records.push(Cheaperprovider);
                        console.log("Ingresa Cheaperprovider" + Cheaperprovider.id);
                    });
                } else
                {
                    console.log("searchCriteria byVehicle" + $scope.searchCriteria);
                    svc.findCheaperbyVehicle($scope.text2Search).then(function (CheaperVehicle) {
                        $scope.records = [];
                        $scope.records.push(CheaperVehicle);
                        console.log("Ingresa Cheaperprovider" + CheaperVehicle.id);
                    });
                }
            };

            $scope.listItems = function () {
                $scope.findItem();
            };
            $scope.enableSubmit = function(){
                $scope.varEnable = false;
            }
                        
           this.selectProduct = function (current) {
               svc.setSelectedProductId(current.id);
               return svc.getSelectedProductId();
           };
           
           this.getSelectedProductId = function () {
               return svc.getSelectedProductId();
           };
           
            this.searchByName = function (vehicleName) {
                var search;
                if (vehicleName) {
                    search = '?q=' + vehicleName;
                }
                $location.url('/catalog' + search);
            };
            
            $("#users").hide();
            if (authSvc.getCurrentUser()) {
                userSvc.isAdmin().then(function (data) {
                    
                    if (data){
                        $("#users").show();
                    } else {
                        $("#users").hide();
                    }
                });
            }
            $(".dropdown-menu > li > a").click(function () {
                $("#users").hide();
            });
            
            this.question='';
            $scope.tmpRecord;

            var self = this;
            this.recordActions = [{
                    name: 'addToCart',
                    displayName: 'Add to Cart',
                    icon: 'shopping-cart',
                    class: 'primary',
                    dataToggle: '',
                    dataTarget: '',
                    fn: function (record) {
                        if (authSvc.getCurrentUser()) {
                            return cartItemSvc.addItem({
                                product: record,
                                name: record.vehicle.name,
                                quantity: 1});
                        } else {
                            $location.path('/login');
                        }
                    },
                    show: function () {
                        return true;
                    }
                },{
                    name: 'askAQuestion',
                    displayName: 'Ask a Question',
                    icon: 'question-sign',
                    class: 'primary',
                    dataToggle:'modal',
                    dataTarget:'#myModalNorm',
                    fn: function (record) {
                        if (authSvc.getCurrentUser()) {
                            $scope.tmpRecord=record;
                        } else {
                            $location.path('/login');
                        }
                    },
                    show: function () {
                        return true;
                    }
                }, {
                    name: 'reviews',
                    displayName: 'Reviews',
                    icon: 'list',
                    class: 'info',
                    dataToggle:'modal',
                    dataTarget:'#modalReviews',
                    fn: function (record) {
                        vehicleSvc.api.get(record.vehicle.id).then(function (data) {
                            self.detailsMode = true;
                            $scope.vehicleRecord = data;
                            console.log($scope.vehicleRecord.reviews);
                        });
                    },
                    show: function () {
                        return !self.detailsMode;
                    }
                }];

//            this.loadRefOptions();
            this.fetchRecords();
            
            this.sendQuestion = function(){
                console.log("askAQuestion");
                //Tmp question
                newQuestion={
                    question: this.question,
                    product:$scope.tmpRecord,
                    provider:$scope.tmpRecord.provider,
                    client:authSvc.getCurrentUser()
                };
                messageSvc.askQuestion(newQuestion);
                //Hide modal
                $('#myModalNorm').modal('hide');
                //clean question
                this.question='';
            };
                        
            // REQ02
            this.comment='';
            this.sendComment = function(currentProduct){
                newComment={
                    description: this.comment,
                    product:currentProduct,
                    date:new Date(),
                    client:authSvc.getCurrentUser()
                };
                commentSvc.sendComment(newComment);
                //clean comment
                this.comment='';
            };
            // REQ 14 y 21
            $scope.rateProduct = function(){
                $('#rateProduct').modal('show');
                this.rating1 = 5;
                $scope.rating = 5;
                return false;
            };
            
            
            $scope.saveRating = function(){
                //svc.saveStatus($scope.orderEdited);
                $('#rateProduct').modal('hide');
                return false;
            };
            
            $scope.rateFunction = function( rating )
            {
                   var _url = 'your service url';

             var data = {
               rating: rating
             };

             $http.post( _url, angular.toJson(data), {cache: false} )
              .success( function( data )
              {
               success(data);
              })
              .error(function(data){
                error(data);
              });

            };
           
           
        }]);
    mod.directive('starRating',
        function() {
            return {
            restrict : 'A',
            template : '<ul class="rating">'
               + ' <li ng-repeat="star in stars" ng-class="star" ng-click="toggle($index)">'
               + '  <i class="fa fa-star-o"></i>'
               + ' </li>'
               + '</ul>',
            scope : {
             ratingValue : '=',
             max : '=',
             onRatingSelected : '&'
            },
            link : function(scope, elem, attrs) {
             var updateStars = function() {
              scope.stars = [];
              for ( var i = 0; i < scope.max; i++) {
               scope.stars.push({
                filled : i < scope.ratingValue
               });
              }
             };

             scope.toggle = function(index) {
              scope.ratingValue = index + 1;
              scope.onRatingSelected({
               rating : index + 1
              });
             };

             scope.$watch('ratingValue',
              function(oldVal, newVal) {
               if (newVal) {
                updateStars();
               }
              }
             );
            }
            };
        }
);
        
})(window.angular);
