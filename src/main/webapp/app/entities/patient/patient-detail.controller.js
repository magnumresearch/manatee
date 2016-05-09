(function() {
    'use strict';

    angular
        .module('manateeApp')
        .controller('PatientDetailController', PatientDetailController);

    PatientDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Patient', 'ReferralSource', 'EntityAuditService'];

    function PatientDetailController($scope, $rootScope, $stateParams, entity, Patient, ReferralSource, EntityAuditService) {
        var vm = this;
        vm.patient = entity;
        
        var unsubscribe = $rootScope.$on('manateeApp:patientUpdate', function(event, result) {
            vm.patient = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.loadPatientHistory = function() {
            EntityAuditService.findByEntity("com.fangzhou.manatee.domain.Queue", 9999).then(function (data) {
                var audits = data.map(function(it){
                    it.entityValue = JSON.parse(it.entityValue);
                    return it;
                });

                var array_records = [];
                for (var i in audits) {                
                    if(typeof audits[i] ==="object")
                        if ('id' in audits[i]) {
                            var entityValue = audits[i]['entityValue'];
                            if ('patient' in entityValue) {
                                if(typeof entityValue['patient'] ==="object") {
                                    var patient = entityValue['patient'];
                                    var patient_id = patient[
                                    'id']
                                    if (patient_id==entity['id']) {
                                        if('team' in entityValue) {
                                            var team = entityValue['team'];
                                            array_records.push({'teamId': team['id'], 'teamName': team['name'], 'lastModifiedDate': entityValue['lastModifiedDate'], 'lastModifiedBy': entityValue['lastModifiedBy'], 'action': audits[i]['action'], 'potentialDischarged': entityValue['status']});
                                        }
                                    }
                                }
                            }
                        }
                }  
                // console.log("vm.audits");
                // console.log(array_records);
                // console.log(entity);
                $scope.patientHistories = array_records;
            }, function(){
                // vm.loading = false;
            });
        }
        $scope.loadPatientHistory();

    }
})();
