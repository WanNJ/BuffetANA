/**
 * Created by slow_time on 2017/5/16.
 */
let strategyTool = require('../../../bl/strategy/strategyTool');
let describe = require("mocha").describe;
let it = require("mocha").it;

let expect = require('chai').expect;


describe('#compareTo()', function () {
    it('比较两个策略是否相同', function () {
        let strategy1 = {
            beginDate: "2015-01-01T00:00:00.000Z",
            endDate: "2017-03-17T00:00:00.000Z",
            stockPoolConditionVO: { excludeST: false,
                                    industries: [],
                                    benches: [],
                                    stockPool: '沪深300' },
            rank: { r1: [ 'MA', 'asd', 10, 1 ], r2: [ 'MOM', 'desc', 9, 1 ] },
            filter: { f1: [ 'turnOverRate', '<', 1 ] },
            tradeModelVO: { loseRate: null, winRate: null, holdingNums: 2, holdingDays: 2 },
            envSpecDay: 3,
            markNormal: { profitAbility: 7,
                          stability: 16,
                          chooseStockAbility: 11,
                          absoluteProfit: 8,
                          antiRiskAbility: 17,
                          strategyScore: 59 },

            markHS: { profitAbility: 9,
                      stability: 17,
                      chooseStockAbility: 10,
                      absoluteProfit: 1,
                      antiRiskAbility: 19,
                      strategyScore: 56 },
            markHO: { profitAbility: 9,
                      stability: 17,
                      chooseStockAbility: 12,
                      absoluteProfit: 6,
                      antiRiskAbility: 18,
                      strategyScore: 62 },
            markLS: { profitAbility: 16,
                      stability: 12,
                      chooseStockAbility: 11,
                      absoluteProfit: 18,
                      antiRiskAbility: 18,
                      strategyScore: 75 },
            markLO: { profitAbility: 3,
                      stability: 17,
                      chooseStockAbility: 10,
                      absoluteProfit: 0,
                      antiRiskAbility: 17,
                      strategyScore: 47 }
        };
        let strategy2 = {
            beginDate: "2015-01-01T00:00:00.000Z",
            endDate: "2017-03-17T00:00:00.000Z",
            stockPoolConditionVO: { excludeST: false,
                industries: [],
                benches: [],
                stockPool: '沪深300' },
            rank: { r1: [ 'MA', 'asd', 10, 1 ], r2: [ 'MOM', 'desc', 9, 1 ] },
            filter: { f1: [ 'turnOverRate', '<', 1 ] },
            tradeModelVO: { loseRate: null, winRate: null, holdingNums: 2, holdingDays: 2 },
            envSpecDay: 3,
            markNormal: { profitAbility: 7,
                stability: 16,
                chooseStockAbility: 11,
                absoluteProfit: 8,
                antiRiskAbility: 17,
                strategyScore: 59 },

            markHS: { profitAbility: 9,
                stability: 17,
                chooseStockAbility: 10,
                absoluteProfit: 1,
                antiRiskAbility: 19,
                strategyScore: 56 },
            markHO: { profitAbility: 9,
                stability: 17,
                chooseStockAbility: 12,
                absoluteProfit: 6,
                antiRiskAbility: 18,
                strategyScore: 62 },
            markLS: { profitAbility: 16,
                stability: 12,
                chooseStockAbility: 11,
                absoluteProfit: 18,
                antiRiskAbility: 18,
                strategyScore: 75 },
            markLO: { profitAbility: 3,
                stability: 17,
                chooseStockAbility: 10,
                absoluteProfit: 0,
                antiRiskAbility: 17,
                strategyScore: 47 }
        };
        let isSame = strategyTool.compareTo(strategy1, strategy2);
        expect(isSame).to.be.equal(true);
    });
});