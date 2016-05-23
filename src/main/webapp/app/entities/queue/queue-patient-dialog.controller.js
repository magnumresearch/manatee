(function() {
    'use strict';

    angular
        .module('manateeApp')
        .controller('QueuePatientDialogController', QueuePatientDialogController);

    QueuePatientDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Patient', 'ReferralSource','Team','Queue'];

    function QueuePatientDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Patient, ReferralSource,Team,Queue) {
        var vm = this;
        vm.teams = Team.query();
        vm.queue = entity;
        vm.patient = entity;
        vm.referralsources = ReferralSource.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('manateeApp:patientUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        var onNewPatientSaveSuccess = function (result) {
            $scope.$emit('manateeApp:patientUpdate', result);

            if (vm.patient.team !== null) {
            		var newqueue = {};
                	newqueue.team = vm.patient.team;
                	newqueue.patient = result;
                	newqueue.status=null;
                	newqueue.timestampInitial=null;
                	newqueue.timestampFinal=null;
                	newqueue.id=null;
                	console.log(newqueue);
                	Queue.save(newqueue, onSaveSuccess, onSaveError);
             }

            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.patient.id !== null) {
                Patient.update(vm.patient, onNewPatientSaveSuccess, onSaveError);
            } else {
            		Patient.save(vm.patient, onNewPatientSaveSuccess, onSaveError);	             		
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.deadline = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
