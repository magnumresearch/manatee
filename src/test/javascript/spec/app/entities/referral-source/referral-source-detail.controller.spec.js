'use strict';

describe('Controller Tests', function() {

    describe('ReferralSource Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockReferralSource;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockReferralSource = jasmine.createSpy('MockReferralSource');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ReferralSource': MockReferralSource
            };
            createController = function() {
                $injector.get('$controller')("ReferralSourceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'manateeApp:referralSourceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
