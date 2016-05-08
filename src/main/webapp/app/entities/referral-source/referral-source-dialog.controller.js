(function() {
    'use strict';

    angular
        .module('manateeApp')
        .controller('ReferralSourceDialogController', ReferralSourceDialogController);

    ReferralSourceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ReferralSource'];

    function ReferralSourceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ReferralSource) {
        var vm = this;
        vm.referralSource = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('manateeApp:referralSourceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.referralSource.id !== null) {
                ReferralSource.update(vm.referralSource, onSaveSuccess, onSaveError);
            } else {
                ReferralSource.save(vm.referralSource, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
